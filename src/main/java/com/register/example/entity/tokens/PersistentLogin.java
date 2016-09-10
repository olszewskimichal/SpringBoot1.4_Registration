package com.register.example.entity.tokens;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "PERSISTENT_LOGINS")
@Data
@NoArgsConstructor
public class PersistentLogin implements Serializable {

    @Id
    private String series;

    @Column(name = "USERNAME", unique = true, nullable = false)
    private String username;

    @Column(name = "TOKEN", unique = true, nullable = false)
    private String token;

    @Temporal(TemporalType.TIMESTAMP)
    private Date last_used;


}
