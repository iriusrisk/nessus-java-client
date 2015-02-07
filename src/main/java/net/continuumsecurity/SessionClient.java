package net.continuumsecurity;

import javax.security.auth.login.LoginException;

/**
 * Created by stephen on 07/02/15.
 */
public interface SessionClient {
    void login(String username, String password) throws LoginException;

    void logout();
}
