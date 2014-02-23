package net.continuumsecurity;

import net.continuumsecurity.model.jaxrs.NessusReply;

import javax.security.auth.login.LoginException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import java.util.logging.Logger;

/**
 * Created by stephen on 23/02/2014.
 */
public class BaseClient {
    private Client client;
    WebTarget target;
    String token;
    static Logger log = Logger.getLogger(ScanClient.class.toString());

    public BaseClient(String nessusUrl) {
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

    protected String generateSeqNum() {
        return "29823987434";
    }

    protected NessusReply sendRequestAndCheckError(WebTarget target, Form form) {
        NessusReply reply = target.request(MediaType.APPLICATION_XML_TYPE)
                .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), NessusReply.class);
        if (!"OK".equalsIgnoreCase(reply.getStatus())) throw new NessusException("Error: Got status: "+reply.getStatus()+" for request to: "+target.getUri());
        return reply;
    }

    protected String getStringResponse(WebTarget target, Form form) {
        return target.request(MediaType.APPLICATION_XML_TYPE)
                .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);

    }

    protected Form prepopulateForm() {
        Form form = new Form();
        form.param("seq", generateSeqNum());
        form.param("token", token);
        return form;
    }
}
