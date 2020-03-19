package com.example.RESTful.social.qq.config;

import com.example.RESTful.properties.SikieduSecurityProperties;
import com.example.RESTful.repository.JdbcUsersConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;

import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SikieduSecurityProperties sikieduSecurityProperties;

    @Autowired
    private ConnectionFactoryLocator connectionFactoryLocator;

    @Autowired
    private ConnectionSignUp connectionSignUp;

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource,connectionFactoryLocator, Encryptors.noOpText());
        repository.setConnectionSignUp(connectionSignUp);
        return repository;
    }

    @Bean
    public SpringSocialConfigurer sikieduSocialSecurity(){

        String filterProcessesUrl = sikieduSecurityProperties.getQqProperties().getFilterProcessesUrl();
        SikieduSpringSocialConfigurer configurer = new SikieduSpringSocialConfigurer(filterProcessesUrl);
        configurer.signupUrl(sikieduSecurityProperties.getSignUpUrl());

        return  configurer;
    }

    @Bean
    public ProviderSignInUtils providerSignInUtils(){
        return new ProviderSignInUtils(connectionFactoryLocator,getUsersConnectionRepository(connectionFactoryLocator));
    }
}
