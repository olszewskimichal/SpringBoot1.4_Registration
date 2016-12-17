package com.register.example.forms;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

public class ProfileForm {
    @Email
    @NotEmpty
    private String email;

    private List<Cokolwiek> forms;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Cokolwiek> getForms() {
        if (forms == null) {
            forms = new ArrayList<>();
        }
        return forms;
    }

    public void setForms(List<Cokolwiek> forms) {
        this.forms = forms;
    }

    @Override
    public String toString() {
        return "ProfileForm{" +
                "email='" + email + '\'' +
                ", forms=" + forms +
                '}';
    }
}
