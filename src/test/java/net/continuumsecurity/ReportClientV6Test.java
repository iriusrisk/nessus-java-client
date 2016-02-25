package net.continuumsecurity;

import net.continuumsecurity.v5.ReportClientV5;
import net.continuumsecurity.v5.model.Issue;
import net.continuumsecurity.v5.model.jaxrs.Host;
import net.continuumsecurity.v5.model.jaxrs.Port;
import net.continuumsecurity.v6.ReportClientV6;
import net.continuumsecurity.v6.model.HostV6;
import net.continuumsecurity.v6.model.Vulnerability;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by stephen on 08/02/15.
 */
public class ReportClientV6Test implements BaseTest {
    static ReportClientV6 client;
    String scanId = "25";


    @BeforeClass
    public static void setup() throws LoginException {
        client = new ReportClientV6(NESSUS_URL, true);
        client.login(USER, PASSWORD);
    }

    @Test
    public void testGetHostsFromReport() {
        List<HostV6> hosts = client.getAllHosts(scanId);
        assertThat(hosts.size(), equalTo(2));
        assertThat(hosts.get(0).getHostname(), equalTo("192.168.0.3"));
        assertThat(hosts.get(1).getHostname(), equalTo("192.168.0.1"));
    }

    @Test
    public void testGetVulnerabilities() {
        List<Vulnerability> vulns = client.getVulnerabilities(scanId,4);
        assertThat(vulns.get(0).getPluginId(), Matchers.equalTo(10150));
        assertThat(vulns.get(vulns.size()-1).getPluginId(), Matchers.equalTo(70658));
    }

    @Test
    public void testGetAllIssues() {
        Map<Integer,Issue> issues = client.getAllIssuesSortedByPluginId(scanId);
        Issue issue = issues.get(11219);
        assertThat(issue.getHostnames().get(0), Matchers.equalTo("192.168.0.3"));
        assertThat(issue.getHostnames().get(1), Matchers.equalTo("192.168.0.1"));
        assertThat(issue.buildV6Url(issue.getHostsV6().get(0)), Matchers.equalTo("https://localhost:8834/nessus6.html#/scans/25/hosts/4/vulnerabilities/11219"));
    }

}
