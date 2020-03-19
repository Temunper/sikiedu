package com.example.RESTful.social.weixin.api;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class WeixinImpl extends AbstractOAuth2ApiBinding implements Weixin {

    private static final String URL_GET_USERINFO = "https://api.weixin.qq.com/sns/userinfo?openid=";

    private ObjectMapper objectMapper = new ObjectMapper();

    public WeixinImpl(String access_token){
        super(access_token, TokenStrategy.ACCESS_TOKEN_PARAMETER);
    }

    @Override
    protected List<HttpMessageConverter<?>> getMessageConverters() {
        List<HttpMessageConverter<?>> messageConverters =  super.getMessageConverters();
        messageConverters.remove(0);
        messageConverters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return messageConverters;
    }

    @Override
    public WeixinUserInfo getWeixinInfo(String openId) {
        String url = URL_GET_USERINFO+openId;

        String response = getRestTemplate().getForObject(url,String.class);
        WeixinUserInfo weixinUserInfo = null;

        try {
            weixinUserInfo = objectMapper.readValue(response,WeixinUserInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weixinUserInfo;
    }

}
