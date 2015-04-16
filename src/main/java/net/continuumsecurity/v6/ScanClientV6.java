package net.continuumsecurity.v6;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import net.continuumsecurity.PolicyNotFoundException;
import net.continuumsecurity.ScanClient;
import net.continuumsecurity.ScanNotFoundException;
import net.continuumsecurity.v6.model.CreateScanRequest;
import net.continuumsecurity.v6.model.Policies;
import net.continuumsecurity.v6.model.PolicyV6;
import net.continuumsecurity.v6.model.ScanResponseWrapper;
import net.continuumsecurity.v6.model.ScanV6;
import net.continuumsecurity.v6.model.ScansV6;
import net.continuumsecurity.v6.model.Settings;

/**
 * Created by stephen on 07/02/15.
 */
public class ScanClientV6 extends SessionClientV6 implements ScanClient {
	public ScanClientV6(String nessusUrl, boolean acceptAllHostNames) {
		super(nessusUrl, acceptAllHostNames);
	}

	public String getScanStatus(String id) throws ScanNotFoundException {
		WebTarget scanTarget = target.path("/scans");
		int scanId = Integer.parseInt(id);
		ScansV6 scans = getRequest(scanTarget, ScansV6.class);
		for(ScanV6 scan : scans.getScans()){
			if(scanId == scan.getId()){
				return scan.getStatus();
			}
		}
		throw new ScanNotFoundException("No scan with Id: " + id);
	}

	private PolicyV6 getPolicyV6ByName(String name) {
		WebTarget scanTarget = target.path("/policies");
		Policies reply = getRequest(scanTarget, Policies.class);
		for(PolicyV6 policy : reply.getPolicies()){
			if(name.equalsIgnoreCase(policy.getName()))
				return policy;
		}
		throw new PolicyNotFoundException("No policy with name: " + name);
	}

	public int getPolicyIDFromName(String name) throws PolicyNotFoundException {
		return getPolicyV6ByName(name).getId();
	}

	public String getPolicyUUIDFromName(String name) throws PolicyNotFoundException {
		return getPolicyV6ByName(name).getUuid();
	}

	public String newScan(String scanName, String policyName, String targets) {
		PolicyV6 policy = getPolicyV6ByName(policyName);
		WebTarget scanTarget = target.path("scans");
		CreateScanRequest scanCommand = new CreateScanRequest();
		scanCommand.setUuid(policy.getUuid());
		Settings settings = new Settings();
		settings.setName(scanName);
		settings.setPolicy_id(policy.getId());
		settings.setText_targets(targets);
		scanCommand.setSettings(settings);
		//String response = postRequest(scanTarget,scanCommand,String.class);
		ScanResponseWrapper response = postRequest(scanTarget, scanCommand, ScanResponseWrapper.class);
		if(response.getScan() == null || response.getScan().getId() <= 0)
			throw new RuntimeException("Error creating scan: " + response.getError());
		launchScan(response.getScan().getId());
		return Integer.toString(response.getScan().getId()); //Nessus v5 uses the UUID instead of scanId
	}

	public ScansV6 listScans() {
		WebTarget scanTarget = target.path("scans");
		return getRequest(scanTarget, ScansV6.class);
	}

	public void launchScan(int id) {
		WebTarget scanTarget = target.path("scans").path(Integer.toString(id)).path("launch");
		Response response = postRequest(scanTarget, "", Response.class);
		if(response.getStatus() != 200)
			throw new RuntimeException("Error launching scan with ID: " + id + ": " + response.getStatusInfo().getReasonPhrase());
	}

	public boolean isScanRunning(String scanId) {
		try{
			if("completed".equalsIgnoreCase(getScanStatus(scanId)))
				return false;
			if("running".equalsIgnoreCase(getScanStatus(scanId)) || "paused".equalsIgnoreCase(getScanStatus(scanId)))
				return true;
		}catch(ScanNotFoundException e){
			return false;
		}
		return false;
	}
}