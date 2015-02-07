package net.continuumsecurity.v6;

import net.continuumsecurity.PolicyNotFoundException;
import net.continuumsecurity.ScanClient;
import net.continuumsecurity.ScanNotFoundException;
import net.continuumsecurity.v6.model.*;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.UUID;

/**
 * Created by stephen on 07/02/15.
 */
public class ScanClientV6 extends SessionClientV6 implements ScanClient {
    public ScanClientV6(String nessusUrl, boolean acceptAllHostNames) {
        super(nessusUrl,acceptAllHostNames);
    }

    @Override
    public String getScanStatus(String name) throws ScanNotFoundException {
        WebTarget scanTarget = target.path("/scans");

        for (ScanV6 scan : getRequest(scanTarget, ScansV6.class).getScans()) {
            if (name.equalsIgnoreCase(scan.getName())) {
                return scan.getStatus();
            }
        }
        throw new ScanNotFoundException("No scan with name: "+name);
    }

    @Override
    public int getPolicyIDFromName(String name) throws PolicyNotFoundException {
        WebTarget scanTarget = target.path("/policies");
        Policies reply = getRequest(scanTarget,Policies.class);
        for (PolicyV6 policy : reply.getPolicies()) {
            if (name.equalsIgnoreCase(policy.getName())) return policy.getId();
        }
        throw new PolicyNotFoundException("No policy with name: "+name);
    }

    @Override
    public String newScan(String scanName, String policyName, String targets) {
        //first get the policy ID for the name
        int policyId = getPolicyIDFromName(policyName);

        WebTarget scanTarget = target.path("scans");
        CreateScanRequest scanCommand = new CreateScanRequest();
        scanCommand.setUuid(UUID.randomUUID().toString());
        Settings settings = new Settings();
        settings.setName(scanName);
        settings.setPolicy_id(policyId);
        settings.setText_targets(targets);
        scanCommand.setSettings(settings);
        ScanResponse response = postRequest(scanTarget,scanCommand,ScanResponse.class);
        if (response.getUuid() == null || response.getUuid().length() == 0) throw new RuntimeException("Error creating scan: "+response.getError());
        launchScan(response.getId());
        return response.getUuid();
    }

    private void launchScan(int id) {
        WebTarget scanTarget = target.path("scans").path(Integer.toString(id)).path("launch");
        Response response = postRequest(scanTarget,"",Response.class);
        if (response.getStatus() != 200) throw new RuntimeException("Error launching scan with ID: "+id+": "+response.getStatusInfo().getReasonPhrase());
    }

    @Override
    public boolean isScanRunning(String scanName) {
        try {
            getScanStatus(scanName);
            return true;
        } catch (ScanNotFoundException e) {
            return false;
        }
    }
}
