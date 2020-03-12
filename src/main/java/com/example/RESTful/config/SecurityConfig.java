package com.example.RESTful.config;

import com.example.RESTful.filter.ValidateCodeFilter;
import com.example.RESTful.handler.LoginFailureHandler;
import com.example.RESTful.handler.LoginSuccessHandler;
import com.example.RESTful.properties.SikieduSecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Autowired
    private SikieduSecurityProperties sikieduSecurityProperties;

    @Autowired
    private DataSource dataSource;

    @Qualifier("UserService")
    @Autowired
    private UserDetailsService userDetailsService;

    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity)throws Exception{
        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setAuthenticationFailureHandler(loginFailureHandler);
        validateCodeFilter.setSikieduSecurityProperties(sikieduSecurityProperties);
        validateCodeFilter.afterPropertiesSet();

        httpSecurity
                .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginPage("/require")
                .loginProcessingUrl("/loginPage")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)
                .and()
                .rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(sikieduSecurityProperties.getRememberMeSeconds())
                .userDetailsService(userDetailsService)
                .and()
                .authorizeRequests()
                .antMatchers("/login.html","/require","/code/image","/code/sms").permitAll()
                .anyRequest()
                .authenticated()
                .and().csrf().disable()
        ;
    }
}
