package net.continuumsecurity;

import net.continuumsecurity.v5.model.Issue;

import java.util.Map;

/**
 * Created by stephen on 08/02/15.
 */
public interface ReportClient extends SessionClient {
    Map<Integer,Issue> getAllIssuesSortedByPluginId(String uuid);
}
