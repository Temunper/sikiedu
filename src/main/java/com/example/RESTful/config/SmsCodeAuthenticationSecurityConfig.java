package com.example.RESTful.config;

import com.example.RESTful.filter.SmsCodeAuthenticationFilter;
import com.example.RESTful.handler.LoginFailureHandler;
import com.example.RESTful.handler.LoginSuccessHandler;
import com.example.RESTful.provider.SmsAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class SmsCodeAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private LoginFailureHandler failureHandler;
    @Autowired
    private LoginSuccessHandler successHandler;

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        SmsCodeAuthenticationFilter smsCodeAuthenticationFilter = new SmsCodeAuthenticationFilter();

        smsCodeAuthenticationFilter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));
smsCodeAuthenticationFilter.setAuthenticationFailureHandler(failureHandler);
smsCodeAuthenticationFilter.setAuthenticationSuccessHandler(successHandler);
        SmsAuthenticationProvider smsAuthenticationProvider = new SmsAuthenticationProvider();
        smsAuthenticationProvider.setUserDetailsService(userDetailsService);

        builder.authenticationProvider(smsAuthenticationProvider).addFilterAfter(smsCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


    }
}
