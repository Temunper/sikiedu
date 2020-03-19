package com.example.RESTful.dto;

import com.fasterxml.jackson.annotation.JsonView;

public class User {

    public String id;


    public interface UserSimple{};
    public interface UserDetail extends UserSimple{};


    private String username;
    private String password;

//    public User(String id,String username,String password){
//        this.id = id;
//        this.username = username;
//        this.password = password;
//    }

    @JsonView(UserSimple.class)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonView(UserDetail.class)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonView(UserSimple.class)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
