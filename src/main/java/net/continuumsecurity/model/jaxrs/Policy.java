package net.continuumsecurity.model.jaxrs;

/**
 * Created by stephen on 22/02/2014.
 */
public class Policy {
    int policyID;
    String policyName;

    public int getPolicyID() {
        return policyID;
    }

    public void setPolicyID(int policyID) {
        this.policyID = policyID;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }
}
