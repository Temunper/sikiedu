package com.example.RESTful.social.qq.config;

import com.example.RESTful.properties.QQProperties;
import com.example.RESTful.properties.SikieduSecurityProperties;
import com.example.RESTful.social.qq.connection.provider.QQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.security.AuthenticationNameUserIdSource;

@Configuration
@EnableSocial
public class QQConfig extends SocialConfigurerAdapter {

    @Autowired
    private SikieduSecurityProperties sikieduSecurityProperties;

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment environment) {
        QQProperties config = sikieduSecurityProperties.getQqProperties();
        QQConnectionFactory qqConnectionFactory = new QQConnectionFactory(config.getProviderId(), config.getAppId(), config.getAppSecret());
        connectionFactoryConfigurer.addConnectionFactory(qqConnectionFactory);
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }
}
