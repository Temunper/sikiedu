package com.example.RESTful.validate.processor;

import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ValidateCodeProcessor {

    String SESSION_KEY = "session_key";

    void create(ServletWebRequest request, HttpServletResponse response) throws IOException;
}
