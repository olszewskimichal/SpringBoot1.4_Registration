package com.register.example.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class EmailRegistrationDTO implements Serializable {
    private String email;
    private String topic;
    private String token;
}
