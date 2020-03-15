package com.example.RESTful.repository;

import com.example.RESTful.validate.code.ValidateCode;
import com.example.RESTful.validate.processor.ValidateCodeProcessor;
import com.example.RESTful.validate.type.ValidateCodeType;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

@Component
public class ValidateCodeRepositoryImpl implements ValidateCodeRepository {

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Override
    public void save(ServletWebRequest request, ValidateCode code, ValidateCodeType type) {
        sessionStrategy.setAttribute(request, ValidateCodeProcessor.SESSION_KEY,code);
    }

    @Override
    public ValidateCode get(ServletWebRequest request,ValidateCodeType type) {
        ValidateCode validateCode = (ValidateCode) sessionStrategy.getAttribute(request,ValidateCodeProcessor.SESSION_KEY);
        return validateCode;
    }

    @Override
    public void remove(ServletWebRequest request) {
        sessionStrategy.removeAttribute(request,ValidateCodeProcessor.SESSION_KEY);
    }
}
