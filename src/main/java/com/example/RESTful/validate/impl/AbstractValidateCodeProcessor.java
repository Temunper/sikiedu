package com.example.RESTful.validate.impl;

import com.example.RESTful.exception.ValidateCodeException;
import com.example.RESTful.properties.SikieduSecurityProperties;
import com.example.RESTful.repository.ValidateCodeRepository;
import com.example.RESTful.repository.ValidateCodeRepositoryImpl;
import com.example.RESTful.validate.code.ImageCode;
import com.example.RESTful.validate.code.ValidateCode;
import com.example.RESTful.validate.processor.ValidateCodeProcessor;
import com.example.RESTful.validate.type.ValidateCodeType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractValidateCodeProcessor<T extends ValidateCode> implements ValidateCodeProcessor {

    SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Autowired
    private SikieduSecurityProperties sikieduSecurityProperties;

    @Autowired
    private ValidateCodeRepository validateCodeRepository;

    @Override
    public void create(ServletWebRequest request, HttpServletResponse response) throws IOException {
        T validateCode =  generate(request);

        save(request,validateCode);

        send(request,response,validateCode);
    }

    @Override
    public void validate(ServletWebRequest request) throws ServletRequestBindingException {
        ValidateCodeType codeType = getValidateCodeType();
        T codeInSession = (T) validateCodeRepository.get(request,codeType);
        String codeInRequest;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), codeType.getParamNameOnValidate());
        }catch (ServletRequestBindingException e){
            throw new ValidateCodeException("获取验证码的值失败");
        }

        if (StringUtils.isEmpty(codeInRequest)) {
            throw new ValidateCodeException("验证码不能为空");
        }
        if (codeInSession == null) {
            throw new ValidateCodeException("验证码不存在");
        }
        if (codeInSession.isExpired()) {
            throw new ValidateCodeException("验证码已过期");
        }
        if (!StringUtils.pathEquals(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不匹配");
        }
        validateCodeRepository.remove(request);

    }

    protected abstract T generate(ServletWebRequest request);

    protected void save(ServletWebRequest request, T validateCode) {
        validateCodeRepository.save(request,validateCode,getValidateCodeType());

    }

    protected abstract void send(ServletWebRequest request,HttpServletResponse response, T validateCode) throws IOException;

    public SikieduSecurityProperties getSikieduSecurityProperties() {
        return sikieduSecurityProperties;
    }

    public void setSikieduSecurityProperties(SikieduSecurityProperties sikieduSecurityProperties) {
        this.sikieduSecurityProperties = sikieduSecurityProperties;
    }

    private ValidateCodeType getValidateCodeType(){
        String type = StringUtils.replace(getClass().getSimpleName(),"CodeProcessor","");
        return  ValidateCodeType.valueOf(type.toUpperCase());
    }
}
