package com.example.demo.config.web.handlers;

import com.example.demo.config.web.exceptions.JwtParsedDataDissimilarityException;
import com.fasterxml.jackson.core.JsonParseException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.DeserializationException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleException(ResponseStatusException exception) {

        Map<String,String> body = Map.of(

                "message", exception.getReason(),
                "error",Integer.toString(exception.getStatusCode().value())
        );
        return ResponseEntity
                .status(exception.getStatusCode())
                .body(body);
    }

    //https://stackoverflow.com/questions/17715921/exception-handling-for-filter-in-spring
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<?> handleException(JwtException exception, WebRequest request) {


        Map<String,String> body = Map.of(

                "message", exception.getMessage(),
                "error","401"
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(body);
    }

    @ExceptionHandler(JwtParsedDataDissimilarityException.class)
    public ResponseEntity<?> handleException(JwtParsedDataDissimilarityException exception, WebRequest request) {


        Map<String,String> body = Map.of(

                "message", exception.getMessage(),
                "error","401"
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(body);
    }

}
