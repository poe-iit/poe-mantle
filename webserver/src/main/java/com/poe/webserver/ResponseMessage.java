package com.poe.webserver;

/*
 * ResponseMessage is a generic class to make HTTP responses more readable
 * Type should be one of "Success" or "Error", and message should include more 
 * information for the user
 */
public class ResponseMessage {
    private String type;
    private String message;

    public ResponseMessage(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return this.type;
    }

    public String getMessage() {
        return this.message;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
