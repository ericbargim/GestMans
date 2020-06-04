package com.gestmans.Business.Exceptions;

public class ErorRetrievingInfoException extends Exception {
    String message;
    public ErorRetrievingInfoException() {

    }

    public ErorRetrievingInfoException(String message)
    {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return  message;
    }
}
