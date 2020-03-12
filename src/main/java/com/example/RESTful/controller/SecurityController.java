package com.example.RESTful.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class SecurityController {

    private RequestCache requestCache = new HttpSessionRequestCache();

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    //判断是否以html结尾
    @RequestMapping("/require")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public String require(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SavedRequest savedRequest = requestCache.getRequest(request,response);
        if (savedRequest != null){
            String url = savedRequest.getRedirectUrl();
            if (StringUtils.endsWithIgnoreCase(url,".html")){
                redirectStrategy.sendRedirect(request,response,"/login.html");
            }

        }

        return new String("需要先登录");
    }

//    重定向登录



//    跳转登录
}
