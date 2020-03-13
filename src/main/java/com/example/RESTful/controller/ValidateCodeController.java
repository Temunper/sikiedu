package com.example.RESTful.controller;


import com.example.RESTful.properties.SikieduSecurityProperties;
import com.example.RESTful.validate.code.ImageCode;
import com.example.RESTful.validate.code.ValidateCode;

import com.example.RESTful.validate.processor.ValidateCodeProcessor;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

@RestController
public class ValidateCodeController {

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();


    @Autowired
    private SikieduSecurityProperties sikieduSecurityProperties;

    @Autowired
    private Map<String, ValidateCodeProcessor> validateCodeProcessorMap;

    @GetMapping("/code/{type}")
    public void createSmsCode(HttpServletRequest request, HttpServletResponse response, @PathVariable String type) throws ServletRequestBindingException, IOException {
        validateCodeProcessorMap.get(type+"CodeProcessor").create(new ServletWebRequest(request),response);
    }

}
