package com.qarea.mlfw.util;

public class UnknownWordException extends Exception {

    /**
     * Local variable is serialVersionUID with type long
     */
    private static final long serialVersionUID = -286286100290774764L;

    public UnknownWordException() {
    }

    public UnknownWordException(String message) {
        super(message);
    }
}
