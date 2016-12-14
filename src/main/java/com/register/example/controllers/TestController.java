package com.register.example.controllers;

import com.register.example.builders.UserBuilder;
import com.register.example.entity.Role;
import com.register.example.entity.User;
import com.register.example.entity.test.Dupa;
import com.register.example.entity.test.Test;
import com.register.example.repository.ProductRepository;
import com.register.example.repository.UserRepository;
import com.register.example.repository.test.DupaRepository;
import com.register.example.repository.test.TestRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {
    private final UserRepository userRepository;
    private final TestRepository testRepository;
    private final DupaRepository dupaRepository;
    private final ProductRepository productRepository;

    public TestController(UserRepository userRepository, TestRepository testRepository, DupaRepository dupaRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.testRepository = testRepository;
        this.dupaRepository = dupaRepository;
        this.productRepository = productRepository;
    }

    //  @Cacheable("users")
    @RequestMapping("/test")
    public List<User> test() {
        return userRepository.findAll();
    }

    //   @CacheEvict(value = "users", allEntries = true)
    @RequestMapping("/test2")
    public User test2() {
        return userRepository.save(new UserBuilder("admin11", "admin81").withPassword("admin")
                .withRole(Role.ADMIN)
                .withEnabled(true)
                .build());
    }

    @RequestMapping("/test3")
    public String test3() {
        return testRepository.findALL().toString();
    }

    @RequestMapping("/test4")
    public String test4() {
        dupaRepository.deleteDupaByID(1L);
        return testRepository.findALL().toString();
    }

    @RequestMapping("/test5")
    public String test5() {
        Test test = testRepository.findOne(1L);
        test.getDupas().add(new Dupa("dupa4", test));
        test = testRepository.save(test);
        return test.toString();
    }

    @RequestMapping("/test6")
    public String test6() {
        dupaRepository.deleteDupaByTestId(1L);
        testRepository.deleteTestByID(1L);
        return "pusto";
    }

   /* @Cacheable("products")
    @RequestMapping("/test7")
    public List<Product> test7() {
        return productRepository.findAll();
    }

    @CacheEvict(value = "products", allEntries = true)
    @RequestMapping("/test8")
    public Product test8() {
        return productRepository.save(new Product());
    }*/


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
