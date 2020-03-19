package com.example.RESTful.controller;

import com.example.RESTful.dto.User;
import com.example.RESTful.service.IUserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.ProviderSignInAttempt;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import sun.plugin.dom.core.Element;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private ProviderSignInUtils providerSignInUtils;


    @PostMapping("/register")
    public void register(User user , HttpServletRequest request){
        com.example.RESTful.domain.User domainUser = new com.example.RESTful.domain.User(null,user.getUsername(),user.getPassword());
        userService.register(domainUser);
        String userId = user.getUsername();
        providerSignInUtils.doPostSignUp(userId, new ServletWebRequest(request));

    }


    @GetMapping
    @JsonView(User.UserSimple.class)
    public List<User> query(){
//        System.out.println(username);
        List<User> users = new LinkedList<User>();
        users.add(new User());
        users.add(new User());
        users.add(new User());
        return users;
    }

//    @RequestMapping(value = "/user/{id}",method = RequestMethod.GET)
//    @JsonView(User.UserDetail.class)
//    public User getInfo(@PathVariable String id){
//        User user = new User();
//        user.setUsername("tem");
//        return user;
//    }

    @RequestMapping(value = "/user",method = RequestMethod.POST)
    public User addUser(@RequestBody User user){
        System.out.println(user.getUsername() + "-----" + user.getPassword());
        user.setId("1");
        return user;
    }

    @RequestMapping(value = "/user/{id}" ,method = RequestMethod.PUT)
    public User updateUser(@RequestBody User user){
        System.out.println(user.getUsername()+"-----");
        user.setId("1");
        return user;
    }

    @DeleteMapping("/{id}")
    public User deleteUser(@PathVariable String id){
        System.out.println(id+"-----");
        User user = new User();
        user.setId("1");
        return user;
    }
}
