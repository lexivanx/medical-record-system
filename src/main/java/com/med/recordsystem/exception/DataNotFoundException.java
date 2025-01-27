package com.med.recordsystem.exception;

public class DataNotFoundException extends RuntimeException{
    public DataNotFoundException(String message) {
        super(message);
    }
}