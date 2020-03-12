package com.example.RESTful.service;


import com.example.RESTful.domain.User;
import com.example.RESTful.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("UserService")
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        System.out.println(username);
        User user = userRepository.findUserByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException(username);
        }
        return new User(username,passwordEncoder.encode(user.getPassword()), AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));

    }
}
