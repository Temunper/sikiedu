package com.example.RESTful.social.weixin.api;

public interface Weixin {
    WeixinUserInfo getWeixinInfo(String openId);
}
