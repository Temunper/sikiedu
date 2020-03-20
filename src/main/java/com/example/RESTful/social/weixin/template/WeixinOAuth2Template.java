package com.example.RESTful.social.weixin.template;

import com.example.RESTful.social.weixin.connect.WeixinAccessGrant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class WeixinOAuth2Template extends OAuth2Template {
    private static final String REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token";

    private String clientId;
    private String clientSecret;
    private String accessTokenUrl;

    private ObjectMapper objectMapper = new ObjectMapper();

    public WeixinOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.accessTokenUrl = accessTokenUrl;
    }

    @Override
    public AccessGrant exchangeForAccess(String authorizationCode, String redirectUri, MultiValueMap<String, String> additionalParameters) {
        StringBuilder accessTokenRequestUrl = new StringBuilder(accessTokenUrl);
        accessTokenRequestUrl.append("?appid=").append(clientId);
        accessTokenRequestUrl.append("&secret=").append(clientSecret);
        accessTokenRequestUrl.append("&code=").append(authorizationCode);
        accessTokenRequestUrl.append("&grant_type=authorization_code");
        return getAccessToken(accessTokenRequestUrl);
    }

    private AccessGrant getAccessToken(StringBuilder accessTokenRequestUrl) {

        String response = getRestTemplate().getForObject(accessTokenUrl.toString(), String.class);
        Map<String, Object> result = null;
        try {
            result = objectMapper.readValue(response, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        WeixinAccessGrant weixinAccessGrant = new WeixinAccessGrant((String) result.get("access_token"),
                (String) result.get("scope"),
                (String) result.get("refresh_token"),
                new Long((String) result.get("expires_in")));
        weixinAccessGrant.setOpenid((String) result.get("openid"));
        return weixinAccessGrant;
    }

    public String buildAuthorizeUrl(OAuth2Parameters parameters) {
        String url = super.buildAuthenticateUrl(parameters);
        url = url + "&appid=" + clientId + "&scope=snsapi_login";
        return url;
    }

    @Override
    protected RestTemplate createRestTemplate() {
        RestTemplate template = super.createRestTemplate();
        template.getMessageConverters().add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return template;
    }


    @Override
    public AccessGrant refreshAccess(String refreshToken,  MultiValueMap<String, String> additionalParameters) {
        StringBuilder refreshTokenUrl = new StringBuilder(REFRESH_TOKEN_URL);
        refreshTokenUrl.append("?appid=").append(clientId);
        refreshTokenUrl.append("&grant_type=refresh_token");
        refreshTokenUrl.append("refresh_token=").append(refreshToken);
        return getAccessToken(refreshTokenUrl);
    }

}
