package com.example.RESTful.controller;

import com.example.RESTful.dto.User;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

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
