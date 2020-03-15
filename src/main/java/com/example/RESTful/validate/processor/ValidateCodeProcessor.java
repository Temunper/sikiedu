package com.example.RESTful.validate.processor;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ValidateCodeProcessor {

    String SESSION_KEY = "session_key";

    void create(ServletWebRequest request, HttpServletResponse response) throws IOException;

    void validate(ServletWebRequest request) throws ServletRequestBindingException;
}
