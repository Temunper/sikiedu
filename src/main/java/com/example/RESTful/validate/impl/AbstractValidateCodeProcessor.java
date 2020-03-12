package com.example.RESTful.validate.impl;

import com.example.RESTful.validate.processor.ValidateCodeProcessor;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletResponse;

public abstract class AbstractValidateCodeProcessor implements ValidateCodeProcessor {

    @Override
    public void create(ServletWebRequest request, HttpServletResponse response) {
        generate();

        save();

        send();
    }

    protected abstract void generate();

    protected void save(){

    };

    protected abstract void send();


}
