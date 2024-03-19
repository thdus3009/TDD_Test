package io.hhplus.tdd.exception;

public class DataBaseException extends RuntimeException{
    public DataBaseException(String message){
        super(message+" 에러 발생");
    }
}
