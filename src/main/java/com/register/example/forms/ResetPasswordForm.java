package com.register.example.forms;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
public class ResetPasswordForm {
    @Pattern(regexp = "^(?=.*?[A-Z])(?=(.*[a-z]){1,})(?=(.*[\\d]){1,})(?=(.*[\\W]){1,})(?!.*\\s).{3,}$")
    private String passForm;

    private String confirmPassword;

    private String token;

    public ResetPasswordForm(String token) {
        this.token = token;
    }
}
