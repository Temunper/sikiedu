package com.example.RESTful.filter;

import com.example.RESTful.config.SecurityConstants;
import com.example.RESTful.exception.ValidateCodeException;
import com.example.RESTful.properties.SikieduSecurityProperties;
import com.example.RESTful.validate.code.ImageCode;
import com.example.RESTful.validate.processor.ValidateCodeProcessor;
import com.example.RESTful.validate.processor.ValidateCodeProcessorHolder;
import com.example.RESTful.validate.type.ValidateCodeType;
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
import java.util.*;

public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {

    private AuthenticationFailureHandler authenticationFailureHandler;

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    private SikieduSecurityProperties sikieduSecurityProperties;

    private Map<String, ValidateCodeType> urlMap= new HashMap<String,ValidateCodeType>();

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    ValidateCodeProcessorHolder validateCodeProcessorHolder = new ValidateCodeProcessorHolder();

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        urlMap.put(SecurityConstants.DEFAULT_SIGN_IN_PROCESSOR_FORM,ValidateCodeType.IMAGE);
        addUrlToMap(sikieduSecurityProperties.getCodeProperties().getImageCodeProperties().getUrl(),ValidateCodeType.IMAGE);
        urlMap.put(SecurityConstants.DEFAULT_SIGN_IN_PROCESSOR_SMS,ValidateCodeType.SMS);
        addUrlToMap(sikieduSecurityProperties.getCodeProperties().getSmsCodeProperties().getUrl(),ValidateCodeType.SMS);


//        String[] configUrls = StringUtils.split(sikieduSecurityProperties.getCodeProperties().getImageCodeProperties().getUrl(), ",");
//
//        assert configUrls != null;
//        urls.addAll(Arrays.asList(configUrls));
//        urls.add("/loginPage");

    }

    public void addUrlToMap(String url,ValidateCodeType type){
        if (!StringUtils.isEmpty(url)){
            System.out.println(url);
            String[] urls = StringUtils.split(url,",");
            System.out.println(urls.toString());
            assert urls != null;
            for (String u :
                    urls) {
                urlMap.put(u,type);
            }
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        boolean action = false;
//        for (String url :
//                urls) {
//            if (antPathMatcher.match(url, request.getRequestURI())) {
//                action = true;
//            }
//        }
//        if (action) {
//            try {
//                validate(new ServletWebRequest(request));
//            } catch (ValidateCodeException e) {
//                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
//                return;
//            }
//        }
//        filterChain.doFilter(request, response);
        ValidateCodeType type = getValidateCodeType(request);
        if (type != null){
            try {
                validateCodeProcessorHolder.findValidateCodeProcessor(type).validate(new ServletWebRequest(request));
            }catch (ValidateCodeException e){
                authenticationFailureHandler.onAuthenticationFailure(request,response,e);
                return;
            }

        }
        filterChain.doFilter(request, response);
    }

    private ValidateCodeType getValidateCodeType(HttpServletRequest request) {
        ValidateCodeType t = null;

        if (StringUtils.startsWithIgnoreCase(request.getMethod(),"get")){
            Set<String> urls = urlMap.keySet();
            for (String url :
                    urls) {
                if (antPathMatcher.match(url,request.getRequestURI()))
                {
                    t = urlMap.get(url);
                }
            }
        }
        return t;
    }

    private void validate(ServletWebRequest request) throws ServletRequestBindingException, ValidateCodeException {

        ImageCode codeInSession = (ImageCode) sessionStrategy.getAttribute(request, ValidateCodeProcessor.SESSION_KEY);
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

        sessionStrategy.removeAttribute(request, ValidateCodeProcessor.SESSION_KEY);

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

    public ValidateCodeProcessorHolder getValidateCodeProcessorHolder() {
        return validateCodeProcessorHolder;
    }

    public void setValidateCodeProcessorHolder(ValidateCodeProcessorHolder validateCodeProcessorHolder) {
        this.validateCodeProcessorHolder = validateCodeProcessorHolder;
    }
}
