package net.continuumsecurity.v6;

import net.continuumsecurity.ReportClient;
import net.continuumsecurity.v5.model.Issue;
import net.continuumsecurity.v6.model.HostV6;
import net.continuumsecurity.v6.model.ReportV6;
import net.continuumsecurity.v6.model.Vulnerability;

import javax.ws.rs.client.WebTarget;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by stephen on 08/02/15.
 */
public class ReportClientV6 extends SessionClientV6 implements ReportClient {

    static Logger log = Logger.getLogger(ReportClientV6.class.toString());

    public ReportClientV6(String nessusUrl, boolean acceptAllHostNames) {
        super(nessusUrl,acceptAllHostNames);
    }

    public List<HostV6> getAllHosts(String scanId) {
        WebTarget reportTarget = target.path("scans").path(scanId);
        ReportV6 report = getRequest(reportTarget,ReportV6.class);

        return report.getHosts();
        //Report report = getRequest(reportTarget,Report.class);
        //return report.getHosts().getHosts();
    }

    public List<Vulnerability> getVulnerabilities(String scanId, int hostId) {
        WebTarget reportTarget = target.path("scans").path(scanId).path("hosts").path(Integer.toString(hostId));
        HostV6 hostDetails = getRequest(reportTarget,HostV6.class);
        return hostDetails.getVulnerabilities();
    }

    @Override
    public Map<Integer, Issue> getAllIssuesSortedByPluginId(String scanId) {
        Map<Integer,Issue> issues = new HashMap<Integer,Issue>();
        for (HostV6 host : getAllHosts(scanId)) {
            for (Vulnerability vuln : getVulnerabilities(scanId, host.getHostId())) {
                Issue issue = issues.get(vuln.getPluginId());
                if (issue == null) {
                    issue = vuln.toIssue(nessusUrl,scanId);
                    issues.put(vuln.getPluginId(), issue);
                }
                issue.getHostsV6().add(host);
                issue.getHostnames().add(host.getHostname());
            }
        }
        return issues;
    }
}
