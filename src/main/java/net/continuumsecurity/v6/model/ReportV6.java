package net.continuumsecurity.v6.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by stephen on 08/02/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportV6 {
    List<HostV6> hosts;
    List<Vulnerability> vulnerabilities;

    public List<Vulnerability> getVulnerabilities() {
        return vulnerabilities;
    }

    public void setVulnerabilities(List<Vulnerability> vulnerabilities) {
        this.vulnerabilities = vulnerabilities;
    }

    @XmlElement(name="hosts")
    public List<HostV6> getHosts() {
        return hosts;
    }

    public void setHosts(List<HostV6> hosts) {
        this.hosts = hosts;
    }
}
