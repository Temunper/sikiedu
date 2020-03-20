package com.example.RESTful.social.weixin.connect;

import org.springframework.social.oauth2.AccessGrant;

public class WeixinAccessGrant extends AccessGrant {

    private static final long serialVersionUID = -7232345435324232213L;

    private String openid;

    public WeixinAccessGrant() {
        super("");
    }

    public WeixinAccessGrant(String accessToken, String scope, String refreshToken, Long expiresIn) {
        super(accessToken, scope, refreshToken, expiresIn);
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
