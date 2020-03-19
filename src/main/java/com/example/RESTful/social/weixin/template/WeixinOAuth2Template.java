package com.example.RESTful.social.weixin.template;

import org.springframework.social.oauth2.OAuth2Template;

public class WeixinOAuth2Template extends OAuth2Template {


    public WeixinOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
    }


}
