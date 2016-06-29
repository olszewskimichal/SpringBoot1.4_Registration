package com.register.example.forms;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class UserCreateForm {

    private String name;

    private String lastName;

    @NotBlank
    @Pattern(regexp = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")
    @Size(max = 50)
    private String email;

    private String login;

    private String password;

    private String confirmPassword;


    public UserCreateForm(String name, String lastName, String email, String login, String password, String confirmPassword) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.login = login;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
