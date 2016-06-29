package com.register.example.builders;

import com.register.example.forms.UserCreateForm;

public class UserCreateFormBuilder {
    private String name = "przykladoweImie";
    private String lastName = "przykladoweNazwisko";
    private String email;
    private String login;
    private String password;
    private String confirmPassword;

    public UserCreateFormBuilder(String email, String login) {
        this.email = email;
        this.login = login;
    }

    public UserCreateFormBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserCreateFormBuilder withConfirmPassword(String password) {
        this.confirmPassword = password;
        return this;
    }

    public UserCreateForm build() {
        return new UserCreateForm(name, lastName, email, login, password, confirmPassword);
    }
}
