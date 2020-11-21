package com.bigjpg.exception;

public class ChooseFileException extends Exception {

    private static final long serialVersionUID = 1L;
    private String mMessage;

    public ChooseFileException() {
    }

    public ChooseFileException(String message) {
        mMessage = message;
    }

    public void setMessage(String messsage) {
        mMessage = messsage;
    }

    public String getMessage() {
        return mMessage;
    }

}
