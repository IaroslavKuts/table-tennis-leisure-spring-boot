package com.example.demo.config.web.exceptions;

public class JwtParsedDataDissimilarityException extends RuntimeException{
    public JwtParsedDataDissimilarityException(String message) {
        super(message);
    }
}
