package com.example.RESTful.social.qq.template;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

public class QQOAuth2Template extends OAuth2Template {
    public QQOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
        setUseParametersForClientAuthentication(true);
    }

    @Override
    protected RestTemplate createRestTemplate() {
        RestTemplate restTemplate = super.createRestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }

    @Override
    protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
        String responseStr = getRestTemplate().postForObject(accessTokenUrl, parameters, String.class);
        System.out.println(responseStr);
        String[] items = StringUtils.split(responseStr, "&");
        String[] item = StringUtils.split(items[1], "&");
        String access_token = StringUtils.replace(items[0], "access_token=", "");
        Long expires_in = new Long(StringUtils.replace(item[0], "expires_in=", ""));
        String refresh_token = StringUtils.replace(item[1], "refresh_token=", "");

        return new AccessGrant(access_token, null, refresh_token, expires_in);
    }
}
