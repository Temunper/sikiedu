package com.example.RESTful.repository;

import com.example.RESTful.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User,Long> {

    @Query(value = "select * from user where username = ?1",nativeQuery = true)
    User findUserByUsername(String username);
}
