package com.example.RESTful.repository;

import com.example.RESTful.validate.code.ValidateCode;
import com.example.RESTful.validate.type.ValidateCodeType;
import org.springframework.web.context.request.ServletWebRequest;

public interface ValidateCodeRepository {
    void save(ServletWebRequest request, ValidateCode code, ValidateCodeType type);
    ValidateCode get(ServletWebRequest request,ValidateCodeType type);
    void remove(ServletWebRequest request);
}
