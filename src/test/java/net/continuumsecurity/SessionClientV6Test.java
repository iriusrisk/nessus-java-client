package net.continuumsecurity;

import net.continuumsecurity.v5.ScanClientV5;
import net.continuumsecurity.v6.SessionClientV6;
import org.junit.Before;
import org.junit.Test;

import javax.security.auth.login.LoginException;

/**
 * Created by stephen on 07/02/15.
 */
public class SessionClientV6Test {
    SessionClientV6 client;
    String nessusUrl = "https://localhost:8834";
    String user = "continuum";
    String password = "continuum";
    String policyName = "basic";
    String scanName = "testScan";
    String scanUuid = "e2e44ca3-7a0e-f6f8-73fd-04b127ef3f18f82ed1c65a88a7f8";
    String hostname = "127.0.0.1";
    int port = 22;


    @Before
    public void setup() {
        client = new SessionClientV6(nessusUrl,true);
    }

    @Test
    public void testLoginAndLogout() throws LoginException {
        client.login(user,password);
        client.logout();
    }

}
