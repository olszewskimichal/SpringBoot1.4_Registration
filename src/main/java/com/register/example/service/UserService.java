package com.register.example.service;

import com.register.example.entity.User;
import com.register.example.forms.UserCreateForm;
import com.register.example.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Modifying
    public User create(UserCreateForm form) {
        log.info("tworzenie uzytkownika =" + form.getLogin());
        User user = new User();
        user.setEmail(form.getEmail());
        user.setLastName(form.getLastName());
        user.setLogin(form.getLogin());
        user.setName(form.getName());
        user.setPasswordHash(new BCryptPasswordEncoder().encode(form.getPassword()));
        user = userRepository.save(user);
        log.info("Stworzono uzytkownika o id=" + user.getId());
        return user;
    }

    @Transactional
    @Modifying
    public void delete(long id) {
        log.info("usuwanie uzytkownika o id=" + id);
        userRepository.delete(id);
    }


    public Optional<User> getUserByEmailOrLogin(String value) {
        log.info("Pobieranie uzytkownika {}", value);
        return userRepository.findUserDistinctByEmailOrLogin(value, value);
    }
}
