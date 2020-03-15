package com.example.RESTful.validate.processor;

import com.example.RESTful.exception.ValidateCodeException;
import com.example.RESTful.validate.type.ValidateCodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
@Component
public class ValidateCodeProcessorHolder {

    @Autowired
    private Map<String,ValidateCodeProcessor> validateCodeProcessorMap;

    public ValidateCodeProcessor findValidateCodeProcessor(ValidateCodeType type){
        return findValidateCodeProcessor(type.getParamNameOnValidate());
    }

    private ValidateCodeProcessor findValidateCodeProcessor(String type){
        String name = type + ValidateCodeProcessor.class.getName();
        name = StringUtils.replace(name,"Validate","");
        ValidateCodeProcessor validateCodeProcessor = validateCodeProcessorMap.get(name);
        if (validateCodeProcessor == null){
            throw new ValidateCodeException("验证码处理器"+name+"不存在");
        }
        return validateCodeProcessor;
    }
}
