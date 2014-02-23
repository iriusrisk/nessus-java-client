package net.continuumsecurity;

import net.continuumsecurity.model.jaxrs.Host;
import net.continuumsecurity.model.jaxrs.Port;
import net.continuumsecurity.model.jaxrs.ReportItem;
import org.junit.Before;
import org.junit.Test;

import javax.security.auth.login.LoginException;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by stephen on 22/02/2014.
 */
public class ScanClientTest {
    ScanClient client;
    String nessusUrl = "https://localhost:8834";
    String user = "continuum";
    String password = "continuum";
    String policyName = "test";
    String scanUuid = "e2e44ca3-7a0e-f6f8-73fd-04b127ef3f18f82ed1c65a88a7f8";
    String hostname = "127.0.0.1";
    int port = 22;


    @Before
    public void setup() {
        client = new ScanClient(nessusUrl);
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
