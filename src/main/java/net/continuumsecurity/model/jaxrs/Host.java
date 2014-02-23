package net.continuumsecurity.model.jaxrs;

/**
 * Created by stephen on 23/02/2014.
 */
public class Host {
    String hostname;
    int severity;

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
