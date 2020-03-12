package com.example.RESTful.filter;

import com.example.RESTful.controller.ValidateCodeController;
import com.example.RESTful.exception.ValidateCodeException;
import com.example.RESTful.properties.SikieduSecurityProperties;
import com.example.RESTful.validate.code.ImageCode;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
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

public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {

    private AuthenticationFailureHandler authenticationFailureHandler;

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    private SikieduSecurityProperties sikieduSecurityProperties;

    private Set<String> urls = new HashSet<String>();

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();

        String[] configUrls = StringUtils.split(sikieduSecurityProperties.getCodeProperties().getImageCodeProperties().getUrl(), ",");

        assert configUrls != null;
        urls.addAll(Arrays.asList(configUrls));
        urls.add("/loginPage");

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean action = false;
        for (String url :
                urls) {
            if (antPathMatcher.match(url, request.getRequestURI())) {
                action = true;
            }
        }
        if (action) {
            try {
                validate(new ServletWebRequest(request));
            } catch (ValidateCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void validate(ServletWebRequest request) throws ServletRequestBindingException, ValidateCodeException {

        ImageCode codeInSession = (ImageCode) sessionStrategy.getAttribute(request, ValidateCodeController.sessionKey);
        String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), "imageCode");
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

        sessionStrategy.removeAttribute(request, ValidateCodeController.sessionKey);

    }

    public AuthenticationFailureHandler getAuthenticationFailureHandler() {
        return authenticationFailureHandler;
    }

    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    public SessionStrategy getSessionStrategy() {
        return sessionStrategy;
    }

    public void setSessionStrategy(SessionStrategy sessionStrategy) {
        this.sessionStrategy = sessionStrategy;
    }

    public SikieduSecurityProperties getSikieduSecurityProperties() {
        return sikieduSecurityProperties;
    }

    public void setSikieduSecurityProperties(SikieduSecurityProperties sikieduSecurityProperties) {
        this.sikieduSecurityProperties = sikieduSecurityProperties;
    }
}
