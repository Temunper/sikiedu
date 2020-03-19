package com.example.RESTful.properties;

public class WeixinProperties {
    private String appid;
    private  String appSecret;
    private  String prividerId = "weixin";
    private  String filterProcessesUrl = "/qqUrl";

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getPrividerId() {
        return prividerId;
    }

    public void setPrividerId(String prividerId) {
        this.prividerId = prividerId;
    }

    public String getFilterProcessesUrl() {
        return filterProcessesUrl;
    }

    public void setFilterProcessesUrl(String filterProcessesUrl) {
        this.filterProcessesUrl = filterProcessesUrl;
    }
}
