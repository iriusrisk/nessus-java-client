package net.continuumsecurity;

/**
 * Created by stephen on 22/02/2014.
 */
public class ScanNotFoundException extends RuntimeException {
    public ScanNotFoundException() {
    }

    public ScanNotFoundException(String message) {
        super(message);
    }

    public ScanNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
