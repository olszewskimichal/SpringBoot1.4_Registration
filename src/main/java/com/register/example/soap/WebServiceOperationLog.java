package com.register.example.soap;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@ToString
public class WebServiceOperationLog {
    @Id
    @GeneratedValue
    private Long id;

    private String msgId;

    private Date date;

    private Boolean success;

    @Column(length = 10000)
    private String errorMsg;

}
