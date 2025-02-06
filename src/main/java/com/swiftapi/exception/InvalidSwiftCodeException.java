package com.swiftapi.exception;

public class InvalidSwiftCodeException extends Exception {
    public InvalidSwiftCodeException(String message) {
        super(message);
    }
}
