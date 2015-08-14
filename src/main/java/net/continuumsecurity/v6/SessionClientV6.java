package net.continuumsecurity.v6;

import java.util.logging.Logger;

import javax.security.auth.login.LoginException;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.continuumsecurity.ClientFactory;
import net.continuumsecurity.SessionClient;
import net.continuumsecurity.v6.model.Login;

/**
 * Created by stephen on 07/02/15.
 */
public class SessionClientV6 implements SessionClient {
	private Client				client;
	protected WebTarget			target;
	private String				token;
	private static final String	COOKIE_HEADER	= "X-Cookie";
	private static Logger		log				= Logger.getLogger(ScanClientV6.class.toString());
	protected String			nessusUrl;

	public SessionClientV6(String nessusUrl, boolean acceptAllHostNames) {
		this.nessusUrl = nessusUrl;
		client = ClientFactory.createV6Client(acceptAllHostNames);
		target = client.target(nessusUrl);
	}

	public void login(String username, String password) throws LoginException {
		WebTarget loginTarget = target.path("/session");
		Form form = new Form();
		form.param("username", username);
		form.param("password", password);
		Login reply = loginTarget.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Login.class);
		token = reply.getToken();
		if(token == null || token.length() == 0)
			throw new LoginException("Error logging in");
		log.info("Login OK.  Token: " + token);
	}

	public void logout() {
		WebTarget logoutTarget = target.path("/session");
		Response response = logoutTarget.request(MediaType.APPLICATION_JSON_TYPE).header(COOKIE_HEADER, "token=" + token).delete(Response.class);
		if(response.getStatus() != 200)
			throw new RuntimeException("Error logging out. Received status code: " + response.getStatus() + " " + response.getStatusInfo().getReasonPhrase());
		log.info("Logout: " + response.getStatusInfo().getReasonPhrase());
	}

	protected <T> T postRequest(WebTarget target, Object object, Class<T> returnType) {
		T reply = target.request(MediaType.APPLICATION_JSON_TYPE).header(COOKIE_HEADER, "token=" + token).post(Entity.entity(object, MediaType.APPLICATION_JSON_TYPE), returnType);
		return reply;
	}

	protected <T> T getRequest(WebTarget target, Class<T> returnType) {
		return getRequest(target, returnType, MediaType.APPLICATION_JSON_TYPE);
	}

	protected <T> T getRequest(WebTarget target, Class<T> returnType, MediaType mediaType) {
		T reply = target.request(mediaType).header(COOKIE_HEADER, "token=" + token).get(returnType);
		return reply;
	}
}
