package com.register.example.entity.test;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
public class Dupa implements Serializable {
    @GeneratedValue
    @Id
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "testId",referencedColumnName = "id",nullable = false)
    private Test test;

    private String zupa;

    public Dupa(String zupa,Test test){
        this.zupa=zupa;
        this.test=test;
    }

    public Long getId() {
        return id;
    }

    public String getZupa() {
        return zupa;
    }

    public Test getTest() {
        return test;
    }

    @Override
    public String toString() {
        return "Dupa{" +
                "id=" + id +
                ", zupa='" + zupa + '\'' +
                '}';
    }
}
