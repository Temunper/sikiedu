package com.example.RESTful.social.weixin.connect;

import com.example.RESTful.social.weixin.api.Weixin;
import com.example.RESTful.social.weixin.api.WeixinUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

public class WeixinAdapter implements ApiAdapter<Weixin> {

    private String openid;

    public WeixinAdapter(){

    }

    public WeixinAdapter(String openid){
        this.openid = openid;
    }

    @Override
    public boolean test(Weixin api) {
        return false;
    }

    @Override
    public void setConnectionValues(Weixin api, ConnectionValues values) {
        WeixinUserInfo info = api.getWeixinInfo(openid);
        values.setDisplayName(info.getNickname());
        values.setProviderUserId(info.getOpenid());
        values.setImageUrl(info.getHeadimgurl());
    }

    @Override
    public UserProfile fetchUserProfile(Weixin api) {
        return null;
    }

    @Override
    public void updateStatus(Weixin api, String message) {

    }
}
