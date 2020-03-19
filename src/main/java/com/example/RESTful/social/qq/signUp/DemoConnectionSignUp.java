package com.example.RESTful.social.qq.signUp;

import com.example.RESTful.domain.User;
import com.example.RESTful.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Component;

@Component
public class DemoConnectionSignUp implements ConnectionSignUp {

    @Autowired
    private IUserService userService;

    @Override
    public String execute(Connection<?> connection) {
        User user = new User(null,connection.getDisplayName(),"123");
        userService.register(user);

        return connection.getDisplayName();
    }
}
