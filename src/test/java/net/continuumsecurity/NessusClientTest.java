package net.continuumsecurity;

import org.junit.Before;
import org.junit.Test;

import javax.security.auth.login.LoginException;

import static org.junit.Assert.assertNotEquals;

/**
 * Created by stephen on 22/02/2014.
 */
public class NessusClientTest {
    NessusClient client;
    String nessusUrl = "https://localhost:8834";
    String user = "continuum";
    String password = "continuum";

    @Before
    public void setup() {
        client = new NessusClient(nessusUrl);
    }

    @Test
    public void testLoginWithCorrectCreds() throws LoginException {
        client.login(user,password);
    }

    @Test(expected=LoginException.class)
    public void testLoginWithWrongCreds() throws LoginException {
        client.login(user,"sdfsdfasdf");
    }

    @Test(expected=ScanNotFoundException.class)
    public void testGetScanStatusForNonExistentScan() throws ScanNotFoundException,LoginException {
        client.login(user,password);
        client.getScanStatus("nonexistent333");
    }

    @Test
    public void testGetPolicyIDFromNameWithNameTEST() throws LoginException, PolicyNotFoundException {
        client.login(user,password);
        int id = client.getPolicyIDFromName("test");
        assertNotEquals(0,id);
    }
}
