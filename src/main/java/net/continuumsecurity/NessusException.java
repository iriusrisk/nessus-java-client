package net.continuumsecurity;

/**
 * Created by stephen on 23/02/2014.
 */
public class NessusException extends RuntimeException {
    public NessusException() {
    }

    public NessusException(String message) {
        super(message);
    }

    public NessusException(String message, Throwable cause) {
        super(message, cause);
    }
}
