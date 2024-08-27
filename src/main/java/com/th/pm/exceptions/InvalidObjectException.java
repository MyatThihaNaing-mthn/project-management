package com.th.pm.exceptions;



public class InvalidObjectException extends RuntimeException {
    public InvalidObjectException(String message){
        super(message);
    }
    public InvalidObjectException(String message, Throwable cause){
        super(message, cause);
    }
}
