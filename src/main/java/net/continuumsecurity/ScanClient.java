package net.continuumsecurity;

import net.continuumsecurity.model.jaxrs.*;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;

/**
 * Created by stephen on 21/02/2014.
 */
public class ScanClient extends BaseClient {

    public ScanClient(String nessusUrl) {
        super(nessusUrl);
    }

    public String getScanStatus(String name) throws ScanNotFoundException {
        WebTarget scanTarget = target.path("/scan/list");
        Form form = prepopulateForm();

        NessusReply reply = sendRequestAndCheckError(scanTarget, form);

        for (Scan scan : reply.getContents().getScans().getScan()) {
            if (name.equalsIgnoreCase(scan.getReadableName())) {
                return scan.getStatus();
            }
        }
        throw new ScanNotFoundException("No scan with name: "+name);
    }

    public int getPolicyIDFromName(String name) throws PolicyNotFoundException {
        WebTarget scanTarget = target.path("/policy/list");
        Form form = prepopulateForm();

        NessusReply reply = sendRequestAndCheckError(scanTarget, form);
        for (Policy policy : reply.getContents().getPolicies()) {
            if (name.equalsIgnoreCase(policy.getPolicyName())) return policy.getPolicyID();
        }
        throw new PolicyNotFoundException("No policy with name: "+name);
    }

    public String newScan(String scanName, String policyName, String targets) {
    	//first get the policy ID for the name
    	int policyId = getPolicyIDFromName(policyName);
    	
    	WebTarget scanTarget = target.path("/scan/new");
    	Form form = prepopulateForm();
        form.param("scan_name",scanName);
        form.param("target",targets);
        form.param("policy_id", Integer.toString(policyId));

        NessusReply reply = sendRequestAndCheckError(scanTarget, form);
        return reply.getContents().getScan().getUuid();
    }





}
