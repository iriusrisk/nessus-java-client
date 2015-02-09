package net.continuumsecurity;

import net.continuumsecurity.v6.ScanClientV6;
import net.continuumsecurity.v6.SessionClientV6;
import org.hamcrest.Matchers;
import org.junit.*;

import javax.security.auth.login.LoginException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by stephen on 07/02/15.
 */
public class ScanClientV6Test {
    static ScanClientV6 client;
    static String nessusUrl = "https://localhost:8834";
    static String user = "continuum";
    static String password = "continuum";
    String policyName = "basic";
    String scanName = "testScan";
    String hostname = "127.0.0.1";
    int port = 22;


    @BeforeClass
    public static void setup() throws LoginException {
        client = new ScanClientV6(nessusUrl,true);
        client.login(user,password);
    }

    @AfterClass
    public static void after() {
        client.logout();
    }

    @Test
    public void testGetScanStatusForValidName() throws LoginException {
        String status = client.getScanStatus(scanName);
        assertThat(status,equalTo("paused"));
    }

    @Test(expected=ScanNotFoundException.class)
    public void testGetScanStatusForInValidName() throws LoginException {
        client.getScanStatus("boogywoogy");
    }

    @Test
    public void testGetPolicyIdFromName() {
        int id = client.getPolicyIDFromName(policyName);
        assertThat(id,greaterThan(0));
    }

    @Test(expected=PolicyNotFoundException.class)
    public void testGetPolicyIdFromInvalidName() {
        client.getPolicyIDFromName("oompaloompa");
    }

    @Test
    public void testlLaunchScan() throws InterruptedException {
        String id = client.newScan(scanName,policyName,"127.0.0.1");
        assertThat(client.getScanStatus(id), Matchers.equalTo("running"));
        assertThat(client.isScanRunning(id), Matchers.equalTo(true));
        while (client.isScanRunning(id)) {
            Thread.sleep(3*1000);
        }
        assertThat(client.getScanStatus(id), Matchers.equalTo("completed"));

    }


}
