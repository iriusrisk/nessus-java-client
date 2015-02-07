package net.continuumsecurity;

import net.continuumsecurity.v5.model.Issue;
import net.continuumsecurity.v5.model.jaxrs.Host;
import net.continuumsecurity.v5.model.jaxrs.Port;
import net.continuumsecurity.v5.model.jaxrs.ReportItem;

import java.util.List;
import java.util.Map;

/**
 * Created by stephen on 07/02/15.
 */
public interface ReportClient {
    List<Host> getHostsFromReport(String uuid);

    List<Port> getPortsFromHost(String uuid, String hostname);

    List<ReportItem> getFindingsFromPort(String uuid, String host, int port, String protocol);

    Map<Integer,Issue> getAllIssuesSortedByPluginId(String uuid);
}
