package com.example.RESTful.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


//Spring Boot的测试类
@RunWith(SpringRunner.class)
@SpringBootTest
public class MainController {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void before(){
        //创建独立测试类
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    }

//    @Test
    public void test() throws Exception {
        //发起一个get请求
        String str = mockMvc.perform(MockMvcRequestBuilders.get("/user")
                .param("username","tem")
                //json形式
                .contentType(MediaType.APPLICATION_JSON))
                //返回状态码为200
                .andExpect(MockMvcResultMatchers.status().isOk())
                //期望返回json中的数组为3
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
                .andReturn().getResponse().getContentAsString();
        System.out.println(str);
    }

    @Test
    public void getInfo() throws Exception {
        String str =  mockMvc.perform(MockMvcRequestBuilders.get("/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("tem"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(str);
    }


    @Test
    public void addUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"tem\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    public void updateUser() throws Exception {
        String str = mockMvc.perform(MockMvcRequestBuilders.put("/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"tem\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("tem"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(str);
    }

    @Test
    public void deleteUser() throws Exception {
        String str = mockMvc.perform(MockMvcRequestBuilders.delete("/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"1\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(str);
    }
}
