package com.register.example.forms;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
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




}
