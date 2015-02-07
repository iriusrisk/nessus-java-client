package net.continuumsecurity.v6.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.continuumsecurity.v5.model.jaxrs.Scan;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

/**
 * Created by stephen on 07/02/15.
 */
@JsonIgnoreProperties(value = { "folders","timestamp" })
public class ScansV6 {

    private List<ScanV6> scan;

    @XmlElementWrapper(name = "scans")
    public List<ScanV6> getScans() {
        return scan;
    }

    public void setScans(List<ScanV6> scan) {
        this.scan = scan;
    }

}
