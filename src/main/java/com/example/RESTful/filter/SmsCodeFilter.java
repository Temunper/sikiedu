package com.example.RESTful.filter;

import com.example.RESTful.exception.ValidateCodeException;
import com.example.RESTful.handler.LoginFailureHandler;
import com.example.RESTful.properties.SikieduSecurityProperties;
import com.example.RESTful.validate.code.ValidateCode;
import com.example.RESTful.validate.processor.ValidateCodeProcessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SmsCodeFilter extends OncePerRequestFilter implements InitializingBean {
    private LoginFailureHandler failureHandler;

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    private SikieduSecurityProperties sikieduSecurityProperties;

    private Set<String> urls = new HashSet<String>();

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        String[] configUrls = StringUtils.split(sikieduSecurityProperties.getCodeProperties().getSmsCodeProperties().getUrl(), ",");
        assert configUrls != null;
        urls.addAll(Arrays.asList(configUrls));
        urls.add("/loginMobilePage");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean action = false;
        for (String url :
                urls) {
            if (pathMatcher.match(url,request.getRequestURI())) {
                action = true;
            }
        }
        if (action){
            try {
                validate(new ServletWebRequest(request));
            } catch (ValidateCodeException e) {
                failureHandler.onAuthenticationFailure(request,response,e);
                return;
            }
        }
        filterChain.doFilter(request,response);
    }

    private void validate(ServletWebRequest request) throws ServletRequestBindingException {
        ValidateCode codeInSession = (ValidateCode) sessionStrategy.getAttribute(request, ValidateCodeProcessor.SESSION_KEY);
        String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),"smsCode");
        if (StringUtils.isEmpty(codeInRequest)){
            throw new ValidateCodeException("验证码不能为空");
        }
        if (codeInSession == null){
            throw new ValidateCodeException("验证码不存在");
        }
        if (codeInSession.isExpired()){
            throw new ValidateCodeException("验证码超时");
        }
        if (!codeInSession.getCode().equals(codeInRequest)){
            throw new ValidateCodeException("验证码输入错误");
        }
        sessionStrategy.removeAttribute(request,ValidateCodeProcessor.SESSION_KEY);
    }

    public SikieduSecurityProperties getSikieduSecurityProperties() {
        return sikieduSecurityProperties;
    }

    public void setSikieduSecurityProperties(SikieduSecurityProperties sikieduSecurityProperties) {
        this.sikieduSecurityProperties = sikieduSecurityProperties;
    }

    public LoginFailureHandler getFailureHandler() {
        return failureHandler;
    }

    public void setFailureHandler(LoginFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }
}
