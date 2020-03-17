package com.example.RESTful.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sikiedu.security")
public class SikieduSecurityProperties {
    private LoginType loginType = LoginType.JSON;

    private ValidateCodeProperties codeProperties = new ValidateCodeProperties();

    private QQProperties qqProperties = new QQProperties();

    private int rememberMeSeconds = 36000;

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    public ValidateCodeProperties getCodeProperties() {
        return codeProperties;
    }

    public void setCodeProperties(ValidateCodeProperties codeProperties) {
        this.codeProperties = codeProperties;
    }

    public int getRememberMeSeconds() {
        return rememberMeSeconds;
    }

    public void setRememberMeSeconds(int rememberMeSeconds) {
        this.rememberMeSeconds = rememberMeSeconds;
    }

    public QQProperties getQqProperties() {
        return qqProperties;
    }

    public void setQqProperties(QQProperties qqProperties) {
        this.qqProperties = qqProperties;
    }
}
