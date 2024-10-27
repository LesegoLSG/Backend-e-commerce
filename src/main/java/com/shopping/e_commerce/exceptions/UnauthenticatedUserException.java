package com.shopping.e_commerce.exceptions;

public class UnauthenticatedUserException extends RuntimeException{
    public UnauthenticatedUserException(String message){
        super(message);
    }
}
