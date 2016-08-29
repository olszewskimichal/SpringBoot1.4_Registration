package com.register.example.controllers;

import com.register.example.builders.UserBuilder;
import com.register.example.entity.Role;
import com.register.example.entity.User;
import com.register.example.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {
    private final UserRepository userRepository;

    public TestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cacheable("users")
    @RequestMapping("/test")
    public List<User> test() {
        return userRepository.findAll();
    }
    @CacheEvict(value = "users", allEntries = true)
    @RequestMapping("/test2")
    public User test2(){
       return userRepository.save(new UserBuilder("admin11", "admin81").withPassword("admin").withRole(Role.ADMIN).withEnabled(true).build());
    }

   /* @Cacheable("users")
    @RequestMapping("/test")
    public List<User> test() {
        return users;
    }

    @RequestMapping("/test2")
    @CachePut(value = "users")
    public  List<User> test2(){
        User user= userRepository.save(new UserBuilder("admin11", "admin81").withPassword("admin").withRole(Role.ADMIN).withEnabled(true).build());
        users.add(user);
        return users;
    }*/
}
