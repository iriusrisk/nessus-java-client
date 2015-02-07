package net.continuumsecurity;

import net.continuumsecurity.v5.model.Issue;
import net.continuumsecurity.v5.model.jaxrs.Host;
import net.continuumsecurity.v5.model.jaxrs.Port;
import net.continuumsecurity.v5.model.jaxrs.ReportItem;
import net.continuumsecurity.v5.ReportClientV5;
import org.junit.Before;
import org.junit.Test;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by stephen on 23/02/2014.
 */
public class ReportClientV5Test {
    ReportClientV5 client;
    String nessusUrl = "https://localhost:8834";
    String user = "continuum";
    String password = "continuum";
    String policyName = "test";
    String scanUuid = "e2e44ca3-7a0e-f6f8-73fd-04b127ef3f18f82ed1c65a88a7f8";
    String hostname = "127.0.0.1";
    int port = 22;
    int pluginID = 10267;


    @Before
    public void setup() {
        client = new ReportClientV5(nessusUrl,true);
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

    @Test
    public void testGetIssuesByPluginId() throws LoginException {
        client.login(user,password);
        Map<Integer,Issue> issues = client.getAllIssuesSortedByPluginId(scanUuid);
        assertThat(issues.get(pluginID).getSynopsis(), equalTo("An SSH server is listening on this port."));
    }


}
