package com.register.example.entity.test;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
public class Upa implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "test", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Dupka> dupas;

    public List<Dupka> getDupas() {
        if (dupas == null) dupas = new ArrayList<>();
        return dupas;
    }

    public void setDupas(List<Dupka> dupas) {
        this.dupas = dupas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Test{" +
                "dupas=" + dupas +
                ", id=" + id +
                '}';
    }
}
