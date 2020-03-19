package com.example.RESTful.service;


import com.example.RESTful.domain.User;
import com.example.RESTful.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;

@Service("UserService")
public class UserService implements UserDetailsService, SocialUserDetailsService,IUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return buildUser(username);
    }

    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {


        return (SocialUserDetails) buildUser(userId);

    }

    private UserDetails buildUser(String userId){
        System.out.println(userId);
        User user = userRepository.findUserByUsername(userId);
        if (user == null){
            throw new UsernameNotFoundException(userId);
        }
        return new SocialUser(userId,passwordEncoder.encode(user.getPassword()), AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));

    }

    @Override
    public void register(User user) {
        userRepository.save(user);
    }
}
