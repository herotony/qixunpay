package com.qixunpay.Tools.hessianlite.io;

import java.io.IOException;

/**
 * Created by saosinwork on 2017/9/20.
 */
public class HessianProtocolException extends IOException {

    private Throwable rootCause;

    /**
     * Zero-arg constructor.
     */
    public HessianProtocolException() {
    }

    /**
     * Create the exception.
     */
    public HessianProtocolException(String message) {
        super(message);
    }

    /**
     * Create the exception.
     */
    public HessianProtocolException(String message, Throwable rootCause) {
        super(message);

        this.rootCause = rootCause;
    }

    /**
     * Create the exception.
     */
    public HessianProtocolException(Throwable rootCause) {
        super(String.valueOf(rootCause));

        this.rootCause = rootCause;
    }

    /**
     * Returns the underlying cause.
     */
    public Throwable getRootCause() {
        return rootCause;
    }

    /**
     * Returns the underlying cause.
     */
    public Throwable getCause() {
        return getRootCause();
    }

}
