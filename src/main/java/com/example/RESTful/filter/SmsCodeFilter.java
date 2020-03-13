package com.example.RESTful.filter;

import com.example.RESTful.exception.ValidateCodeException;
import com.example.RESTful.handler.LoginFailureHandler;
import com.example.RESTful.properties.SikieduSecurityProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
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

    private void validate(ServletWebRequest request) {
    }

    public SikieduSecurityProperties getSikieduSecurityProperties() {
        return sikieduSecurityProperties;
    }

    public void setSikieduSecurityProperties(SikieduSecurityProperties sikieduSecurityProperties) {
        this.sikieduSecurityProperties = sikieduSecurityProperties;
    }
}
