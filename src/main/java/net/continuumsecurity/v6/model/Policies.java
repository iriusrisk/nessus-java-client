package net.continuumsecurity.v6.model;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Created by stephen on 07/02/15.
 */
public class Policies {
    List<PolicyV6> policies;

    @XmlElement
    public List<PolicyV6> getPolicies() {
        return policies;
    }

    public void setPolicies(List<PolicyV6> policies) {
        this.policies = policies;
    }
}
