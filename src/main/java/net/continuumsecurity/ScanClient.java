package net.continuumsecurity;

/**
 * Created by stephen on 07/02/15.
 */
public interface ScanClient extends SessionClient {
    String getScanStatus(String name) throws ScanNotFoundException;

    int getPolicyIDFromName(String name) throws PolicyNotFoundException;

    String newScan(String scanName, String policyName, String targets);

    boolean isScanRunning(String scanId);
}
