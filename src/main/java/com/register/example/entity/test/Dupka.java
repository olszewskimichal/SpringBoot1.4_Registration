package com.register.example.entity.test;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
public class Dupka implements Serializable {
    @GeneratedValue
    @Id
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "testId", referencedColumnName = "id", nullable = false)
    private Upa test;

    private String zupa;

    public Dupka(String zupa, Upa test) {
        this.zupa = zupa;
        this.test = test;
    }

    public Long getId() {
        return id;
    }

    public Upa getTest() {
        return test;
    }

    public void setTest(Upa test) {
        this.test = test;
    }

    @Override
    public String toString() {
        return "Dupka{" +
                "id=" + id +
                ", test=" + test.getId() +
                ", zupa='" + zupa + '\'' +
                '}';
    }
}
