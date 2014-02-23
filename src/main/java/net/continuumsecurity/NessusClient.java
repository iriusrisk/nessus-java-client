package net.continuumsecurity;

import net.continuumsecurity.model.*;

import javax.security.auth.login.LoginException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by stephen on 21/02/2014.
 */
public class NessusClient {
    private Client client;
    WebTarget target;
    String token;
    static Logger log = Logger.getLogger(NessusClient.class.toString());

    public NessusClient(String nessusUrl) {
        client = ClientFactory.createInsecureSSLClient();
        target = client.target(nessusUrl);
    }

    public void login(String username, String password) throws LoginException {
        WebTarget loginTarget = target.path("/login");
        Form form = new Form();
        form.param("login", username);
        form.param("password", password);
        form.param("seq", generateSeqNum());

        NessusReply reply = sendRequestAndCheckError(loginTarget, form);

        if (!"OK".equalsIgnoreCase(reply.getStatus())) throw new LoginException("Error logging in");
        token = reply.getContents().getToken();
        log.info("Login OK.  Token: "+token);
    }

    public String getScanStatus(String name) throws ScanNotFoundException {
        WebTarget scanTarget = target.path("/scan/list");
        Form form = prepopulateForm();

        NessusReply reply = sendRequestAndCheckError(scanTarget, form);

        for (Scan scan : reply.getContents().getScans().getScan()) {
            if (name.equalsIgnoreCase(scan.getReadableName())) {
                return scan.getStatus();
            }
        }
        throw new ScanNotFoundException("No scan with name: "+name);
    }

    public int getPolicyIDFromName(String name) throws PolicyNotFoundException {
        WebTarget scanTarget = target.path("/policy/list");
        Form form = prepopulateForm();

        NessusReply reply = sendRequestAndCheckError(scanTarget, form);
        for (Policy policy : reply.getContents().getPolicies()) {
            if (name.equalsIgnoreCase(policy.getPolicyName())) return policy.getPolicyID();
        }
        throw new PolicyNotFoundException("No policy with name: "+name);
    }

    public String newScan(String scanName, String policyName, String targets) {
    	//first get the policy ID for the name
    	int policyId = getPolicyIDFromName(policyName);
    	
    	WebTarget scanTarget = target.path("/scan/new");
    	Form form = prepopulateForm();
        form.param("scan_name",scanName);
        form.param("target",targets);
        form.param("policy_id", Integer.toString(policyId));

        NessusReply reply = sendRequestAndCheckError(scanTarget, form);
        return reply.getContents().getScan().getUuid();
    }

    public List<Host> getHostsFromReport(String uuid) {
        WebTarget reportTarget = target.path("/report/hosts");
        Form form = prepopulateForm();
        form.param("report",uuid);
        //String reply = reportTarget.request(MediaType.APPLICATION_XML_TYPE)
        //        .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);

        NessusReply reply = sendRequestAndCheckError(reportTarget,form);
        return reply.getContents().getHost();
    }

    public List<Port> getPortsFromHost(String uuid,String hostname)  {
        List<Host> hosts = getHostsFromReport(uuid);

        for (Host host : hosts) {
            if (hostname.equalsIgnoreCase(host.getHostname())) {
                WebTarget reportTarget = target.path("/report/ports");
                Form form = prepopulateForm();
                form.param("report",uuid);
                form.param("hostname",hostname);
                NessusReply reply = sendRequestAndCheckError(reportTarget,form);
                return reply.getContents().getPort();
            }
        }
        throw new HostNotFoundException("Hostname: "+hostname+" not found in report: "+uuid);
    }

    public List<ReportItem> getFindingsFromPort(String uuid, String host, int port, String protocol) {
        WebTarget reportTarget = target.path("/report/details");

        Form form = prepopulateForm();
        form.param("report", uuid);
        form.param("hostname",host);
        form.param("port",Integer.toString(port));
        form.param("protocol",protocol);

        System.out.println(getStringResponse(reportTarget,form));
        NessusReply reply = sendRequestAndCheckError(reportTarget,form);
        return reply.getContents().getReportItem();
    }

    private String generateSeqNum() {
        return "29823987434";
    }

    private NessusReply sendRequestAndCheckError(WebTarget target, Form form) {
        NessusReply reply = target.request(MediaType.APPLICATION_XML_TYPE)
                .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), NessusReply.class);
        if (!"OK".equalsIgnoreCase(reply.getStatus())) throw new NessusException("Error: Got status: "+reply.getStatus()+" for request to: "+target.getUri());
        return reply;
    }

    private String getStringResponse(WebTarget target, Form form) {
        return target.request(MediaType.APPLICATION_XML_TYPE)
                .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);

    }

    private Form prepopulateForm() {
        Form form = new Form();
        form.param("seq", generateSeqNum());
        form.param("token", token);
        return form;
    }


}
