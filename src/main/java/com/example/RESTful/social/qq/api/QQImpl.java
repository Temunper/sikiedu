package com.example.RESTful.social.qq.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;
import org.springframework.util.StringUtils;
import java.io.IOException;
import com.fasterxml.jackson.databind.JsonMappingException;


import java.io.IOException;

public class QQImpl extends AbstractOAuth2ApiBinding implements QQ {

    private String appId;
    private String openId;
    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String URL_GET_OPENID = "https://graph.qq.com/oauth2.0/me?access_token=%s";
    private static final String URL_GET_USER_INFO = "https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";

    @Override
    public QQUserInfo getUserInfo() {
        String url = String.format(URL_GET_USER_INFO, appId, openId);
        String result = getRestTemplate().getForObject(url, String.class);
        QQUserInfo userInfo = null;
        try {
            userInfo = objectMapper.readValue(result, QQUserInfo.class);
            userInfo.setOpenId(openId);
            System.out.println(userInfo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    public QQImpl(String access_token, String appId) {
        super(access_token, TokenStrategy.ACCESS_TOKEN_PARAMETER);

        this.appId = appId;

        String url = String.format(URL_GET_OPENID, access_token);

        String result = getRestTemplate().getForObject(url, String.class);

        System.out.println(result);
        result = StringUtils.replace(result,"callback( ","");
        result = StringUtils.replace(result," );","");
        System.out.println(result);
        OpenId id = null;
        try {
            id = objectMapper.readValue(result, OpenId.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        this.openId = id.getOpenid();

    }
}
