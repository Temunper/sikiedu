package com.example.RESTful.validate.processor;

import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletResponse;

public interface ValidateCodeProcessor {

    void create(ServletWebRequest request, HttpServletResponse response);
}
