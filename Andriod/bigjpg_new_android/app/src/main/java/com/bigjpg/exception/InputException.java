package com.bigjpg.exception;

public class InputException extends Exception{

    private String mMessage;

    public InputException(){
    }

    public InputException(String message){
        mMessage = message;
    }

    public void setMessage(String messsage){
        mMessage = messsage;
    }

    public String getMessage(){
        return mMessage;
    }

}
