package net.continuumsecurity.v6.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by stephen on 07/02/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScanV6 {
    String name;
    int id;
    private String status;


    public void setStatus(String status) {
        this.status = status;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement
    public String getStatus() {
        return status;
    }
}
