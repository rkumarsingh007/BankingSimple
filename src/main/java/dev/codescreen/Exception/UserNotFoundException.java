package dev.codescreen.Exception;

public class UserNotFoundException extends Exception{
    private Object response;
    public UserNotFoundException() {
        super();

    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);

    }

    public UserNotFoundException(String message) {
        super(message);

    }

    public UserNotFoundException(Throwable cause) {
        super(cause);

    }

    public UserNotFoundException(Object obj){
        this.response = obj;
    }

    public Object getResponse() {
        return response;
    }
}