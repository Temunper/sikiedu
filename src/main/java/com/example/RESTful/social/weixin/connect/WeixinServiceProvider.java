package com.example.RESTful.social.weixin.connect;

import com.example.RESTful.social.weixin.api.Weixin;
import com.example.RESTful.social.weixin.api.WeixinImpl;
import com.example.RESTful.social.weixin.template.WeixinOAuth2Template;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Operations;

public class WeixinServiceProvider extends AbstractOAuth2ServiceProvider<Weixin> {

    private static final String URL_AUTHORIZE = "https://open.weixin.qq.com/connect/qrconnect";
    private static final String URL_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";

    public WeixinServiceProvider(String appId,String appSecret) {
        super(new WeixinOAuth2Template(appId,appSecret,URL_AUTHORIZE,URL_ACCESS_TOKEN));
    }

    @Override
    public Weixin getApi(String accessToken) {
        return new WeixinImpl(accessToken);
    }
}
