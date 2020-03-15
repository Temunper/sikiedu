package com.example.RESTful.filter;

import com.example.RESTful.token.SmsCodeAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SmsCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String SPRING_SECURITY_FROM_MOBILE_KEY = "mobile";

    private String mobileParameter = SPRING_SECURITY_FROM_MOBILE_KEY;

    private boolean postOnly = true;

    public SmsCodeAuthenticationFilter() {
        super(new AntPathRequestMatcher("/loginMobilePage", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("只有post请求才处理");
        }
        String mobile = obtainMobile(request);
        SmsCodeAuthenticationToken token = new SmsCodeAuthenticationToken(mobile);

        setDetails(request, token);
        return this.getAuthenticationManager().authenticate(token);
    }

    protected void setDetails(HttpServletRequest request, SmsCodeAuthenticationToken token) {
        token.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    protected String obtainMobile(HttpServletRequest request) {
        return request.getParameter(mobileParameter);
    }
}
