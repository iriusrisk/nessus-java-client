package net.continuumsecurity.v6.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by stephen on 08/02/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScanTemplates {
    List<ScanTemplate> templates;

    public List<ScanTemplate> getTemplates() {
        return templates;
    }

    public void setTemplates(List<ScanTemplate> templates) {
        this.templates = templates;
    }
}
