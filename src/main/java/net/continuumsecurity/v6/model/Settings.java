package net.continuumsecurity.v6.model;

/**
 * Created by stephen on 07/02/15.
 */
public class Settings {
    String name;
    String launch;
    int policy_id;
    String text_targets;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLaunch() {
        return launch;
    }

    public void setLaunch(String launch) {
        this.launch = launch;
    }

    public int getPolicy_id() {
        return policy_id;
    }

    public void setPolicy_id(int policy_id) {
        this.policy_id = policy_id;
    }

    public String getText_targets() {
        return text_targets;
    }

    public void setText_targets(String text_targets) {
        this.text_targets = text_targets;
    }
}
