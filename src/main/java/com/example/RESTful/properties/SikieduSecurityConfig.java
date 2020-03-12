package com.example.RESTful.properties;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SikieduSecurityProperties.class)
public class SikieduSecurityConfig {
}
