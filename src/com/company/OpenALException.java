package com.company;

import static org.lwjgl.openal.AL10.*;

public class OpenALException extends RuntimeException {

    public OpenALException() {
    }

    public OpenALException(int err) {
        super("Internal " + (err == AL_INVALID_NAME ? "invalid name" : err == AL_INVALID_ENUM ?
                "invalid enum" : err == AL_INVALID_VALUE ? "invalid value" : err == AL_INVALID_OPERATION ?
                "invalid operation" : "unknown") + " OpenAL exception");
    }

    public OpenALException(String message) {
        super(message);
    }

    public OpenALException(String message, Throwable cause) {
        super(message, cause);
    }
}
