package net.continuumsecurity;

import net.continuumsecurity.model.Host;
import net.continuumsecurity.model.NessusReply;
import net.continuumsecurity.model.Port;
import net.continuumsecurity.model.ReportItem;
import org.junit.Before;
import org.junit.Test;

import javax.security.auth.login.LoginException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by stephen on 22/02/2014.
 */
public class NessusClientTest {
    NessusClient client;
    String nessusUrl = "https://localhost:8834";
    String user = "continuum";
    String password = "continuum";
    String policyName = "test";
    String scanUuid = "e2e44ca3-7a0e-f6f8-73fd-04b127ef3f18f82ed1c65a88a7f8";
    String hostname = "127.0.0.1";
    int port = 22;


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

    @Test
    public void testGetHostsFromReport() throws LoginException {
        client.login(user,password);
        List<Host> hosts = client.getHostsFromReport(scanUuid);
        assertThat(hosts.size(), equalTo(1));
        assertThat(hosts.get(0).getHostname(), equalTo(hostname));
    }

    @Test
    public void testGetPortsFromHost() throws LoginException {
        client.login(user,password);
        List<Port> ports = client.getPortsFromHost(scanUuid,hostname);
        assertThat(ports.size(),greaterThan(2));
    }

    @Test
    public void testGetDetailsFromPort() throws LoginException {
        client.login(user,password);
        List<ReportItem> details = client.getFindingsFromPort(scanUuid,hostname,22,"tcp");
        assertThat(details.size(),greaterThan(0));

    }
}
