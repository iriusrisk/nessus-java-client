package net.continuumsecurity.v6;

import net.continuumsecurity.ClientFactory;
import net.continuumsecurity.NessusException;
import net.continuumsecurity.SessionClient;
import net.continuumsecurity.v5.model.jaxrs.NessusReply;
import net.continuumsecurity.v5.ScanClientV5;
import net.continuumsecurity.v6.model.Login;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientResponse;

import javax.security.auth.login.LoginException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

/**
 * Created by stephen on 07/02/15.
 */
public class SessionClientV6 implements SessionClient {
    private Client client;
    WebTarget target;
    String token;
    static final String COOKIE_HEADER = "X-Cookie";
    static Logger log = Logger.getLogger(ScanClientV5.class.toString());

    public SessionClientV6(String nessusUrl, boolean acceptAllHostNames) {
        client = ClientFactory.createV6Client(acceptAllHostNames);
        target = client.target(nessusUrl);
    }

    @Override
    public void login(String username, String password) throws LoginException {
        WebTarget loginTarget = target.path("/session");
        Form form = new Form();
        form.param("username", username);
        form.param("password", password);

        Login reply = loginTarget.request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Login.class);
        token = reply.getToken();
        if (token == null || token.length() == 0) throw new LoginException("Error logging in");
        log.info("Login OK.  Token: "+token);
    }

    @Override
    public void logout() {
        WebTarget logoutTarget = target.path("/session");
        Response response = logoutTarget.request(MediaType.APPLICATION_JSON_TYPE).header(COOKIE_HEADER,"token="+token).delete(Response.class);
        if (response.getStatus() != 200) throw new RuntimeException("Error logging out. Received status code: "+response.getStatus()+" "+response.getStatusInfo().getReasonPhrase());
        log.info("Logout: " + response.getStatusInfo().getReasonPhrase());
    }

    protected <T> T postRequest(WebTarget target, Object object,Class<T> returnType) {
        T reply = target.request(MediaType.APPLICATION_JSON_TYPE).header(COOKIE_HEADER,"token="+token)
                .post(Entity.entity(object, MediaType.APPLICATION_JSON_TYPE), returnType);
        return reply;
    }

    protected <T> T getRequest(WebTarget target, Class<T> returnType) {
        T reply = target.request(MediaType.APPLICATION_JSON_TYPE).header(COOKIE_HEADER,"token="+token)
                .get(returnType);
        return reply;
    }


}
