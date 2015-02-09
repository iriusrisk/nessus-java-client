package net.continuumsecurity.v6.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Created by stephen on 08/02/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HostV6 {
    String hostname;
    int hostId;
    List<Vulnerability> vulnerabilities;

    @XmlElement(name="hostname")
    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    @XmlElement(name="host_id")
    public int getHostId() {
        return hostId;
    }

    public void setHostId(int hostId) {
        this.hostId = hostId;
    }

    public List<Vulnerability> getVulnerabilities() {
        return vulnerabilities;
    }

    public void setVulnerabilities(List<Vulnerability> vulnerabilities) {
        this.vulnerabilities = vulnerabilities;
    }
}
