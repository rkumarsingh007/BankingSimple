package dev.codescreen.Exception;

public class CurrencyMismatch extends Exception{
    private Object response;
    public CurrencyMismatch() {
        super();

    }

    public CurrencyMismatch(String message, Throwable cause) {
        super(message, cause);

    }

    public CurrencyMismatch(String message) {
        super(message);

    }

    public CurrencyMismatch(Throwable cause) {
        super(cause);

    }

    public CurrencyMismatch(Object obj){
        this.response = obj;
    }

    public Object getResponse() {
        return response;
    }
}