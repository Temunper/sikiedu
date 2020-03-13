package com.example.RESTful.validate.impl;

import com.example.RESTful.properties.SikieduSecurityProperties;
import com.example.RESTful.validate.code.ValidateCode;
import com.example.RESTful.validate.processor.ValidateCodeProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractValidateCodeProcessor<T> implements ValidateCodeProcessor {

    SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Autowired
    private SikieduSecurityProperties sikieduSecurityProperties;

    @Override
    public void create(ServletWebRequest request, HttpServletResponse response) throws IOException {
        T validateCode =  generate(request);

        save(request,validateCode);

        send(request,response,validateCode);
    }

    protected abstract T generate(ServletWebRequest request);

    protected void save(ServletWebRequest request, T validateCode) {
        sessionStrategy.setAttribute(request,SESSION_KEY,validateCode);

    }

    protected abstract void send(ServletWebRequest request,HttpServletResponse response, T validateCode) throws IOException;

    public SikieduSecurityProperties getSikieduSecurityProperties() {
        return sikieduSecurityProperties;
    }

    public void setSikieduSecurityProperties(SikieduSecurityProperties sikieduSecurityProperties) {
        this.sikieduSecurityProperties = sikieduSecurityProperties;
    }
}
