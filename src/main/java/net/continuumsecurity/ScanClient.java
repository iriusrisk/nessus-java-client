package net.continuumsecurity;

/**
 * Created by stephen on 07/02/15.
 */
public interface ScanClient extends SessionClient {
	public String getScanStatus(String name) throws ScanNotFoundException;

	public int getPolicyIDFromName(String name) throws PolicyNotFoundException;

	public String newScan(String scanName, String policyName, String targets);

	public boolean isScanRunning(String scanId);
}