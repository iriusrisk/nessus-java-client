package net.continuumsecurity;

import net.continuumsecurity.model.NessusReply;
import net.continuumsecurity.model.Policy;
import net.continuumsecurity.model.Scan;
import org.glassfish.jersey.client.ClientProperties;

import javax.net.ssl.*;
import javax.security.auth.login.LoginException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
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
        client = createTrustingSSLClient();
        target = client.target(nessusUrl);
    }

    public void login(String username, String password) throws LoginException {
        WebTarget loginTarget = target.path("/login");
        Form form = new Form();
        form.param("login", username);
        form.param("password", password);
        form.param("seq", generateSeqNum());

        NessusReply reply = loginTarget.request(MediaType.APPLICATION_XML_TYPE)
                .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), NessusReply.class);
        if (!"OK".equalsIgnoreCase(reply.getStatus())) throw new LoginException("Error logging in");
        token = reply.getContents().getToken();
        log.info("Login OK.  Token: "+token);
    }

    public String getScanStatus(String name) throws ScanNotFoundException {
        WebTarget scanTarget = target.path("/scan/list");
        Form form = prepopulateForm();

        NessusReply reply = scanTarget.request(MediaType.APPLICATION_XML_TYPE)
                 .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), NessusReply.class);
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

        NessusReply reply = scanTarget.request(MediaType.APPLICATION_XML_TYPE)
                .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), NessusReply.class);
        for (Policy policy : reply.getContents().getPolicies()) {
            if (name.equalsIgnoreCase(policy.getPolicyName())) return policy.getPolicyID();
        }
        throw new PolicyNotFoundException("No policy with name: "+name);
    }

    public String newScan(String targets) {
        
    }

    private String generateSeqNum() {
        return "29823987434";
    }

    private Form prepopulateForm() {
        Form form = new Form();
        form.param("seq", generateSeqNum());
        form.param("token", token);
        return form;
    }

    private Client createTrustingSSLClient() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    @Override
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        HttpsURLConnection.setDefaultHostnameVerifier(
                new javax.net.ssl.HostnameVerifier() {

                    public boolean verify(String hostname,
                                          javax.net.ssl.SSLSession sslSession) {
                        if (hostname.equals("localhost")) {
                            return true;
                        }
                        return false;
                    }
                });

        // Install the all-trusting trust manager
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ClientBuilder.newBuilder().sslContext(sc).property(ClientProperties.PROXY_URI, "http://localhost:8888").build();
    }
}
