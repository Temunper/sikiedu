package com.example.RESTful.handler;

import com.example.RESTful.properties.LoginType;
import com.example.RESTful.properties.SikieduSecurityProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SikieduSecurityProperties sikieduSecurityProperties;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        System.out.println("登录失败");
        if (LoginType.JSON.equals(sikieduSecurityProperties.getLoginType())){
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("Application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(exception));
        }else {
            super.onAuthenticationFailure(request,response,exception);
        }

    }
}
