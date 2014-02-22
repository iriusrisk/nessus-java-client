package net.continuumsecurity.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

/**
 * Created by stephen on 22/02/2014.
 */
public class Contents {
    String token;
    Scans scans;
    List<Policy> policies;

    @XmlElementWrapper(name="policies")
    @XmlElement(name="policy")
    public List<Policy> getPolicies() {
        return policies;
    }

    public void setPolicies(List<Policy> policies) {
        this.policies = policies;
    }

    public Scans getScans() {
        return scans;
    }

    public void setScans(Scans scans) {
        this.scans = scans;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
