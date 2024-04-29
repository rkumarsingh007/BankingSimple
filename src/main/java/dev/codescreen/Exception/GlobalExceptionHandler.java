package dev.codescreen.Exception;

import dev.codescreen.Entity.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public Object handleNoRecordFoundException(UserNotFoundException ex)
    {
        return ex.getResponse();
    }

    @ExceptionHandler(CurrencyMismatch.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    public Object handleNotAcceptableTransactionException(CurrencyMismatch ex){
        return ex.getResponse();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleServerErrorException(Exception ex){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Server Side Error.. We are working on it!!");
        errorResponse.setCode("INTERNAL_SERVER_ERROR");
        return errorResponse;
    }


}
