package com.example.RESTful.exception;

import org.springframework.security.core.AuthenticationException;

public class ValidateCodeException extends AuthenticationException {


    public ValidateCodeException(String msg) {
        super(msg);
        // TODO Auto-generated constructor stub
    }
}
