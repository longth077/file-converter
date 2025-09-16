package com.converter.exception;

/**
 * Exception thrown when an unsupported file format is encountered
 */
public class UnsupportedFormatException extends ConversionException {
    
    public UnsupportedFormatException(String message) {
        super(message);
    }
    
    public UnsupportedFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}