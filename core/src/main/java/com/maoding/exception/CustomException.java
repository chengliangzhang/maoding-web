package com.maoding.exception;

/**
 * Created by Idccapp22 on 2016/9/13.
 */
public class CustomException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    private String detailMessage;

    public CustomException(String detailMessage) {
        super(detailMessage);
        this.detailMessage = detailMessage;
    }

    public String getMessage() {
        return detailMessage;
    }

    @Override
    public String toString(){
        return this.getMessage();
    }
}