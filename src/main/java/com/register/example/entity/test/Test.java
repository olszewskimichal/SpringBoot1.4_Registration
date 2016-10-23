package com.register.example.entity.test;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
public class Test implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "test", fetch = FetchType.EAGER, cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Dupa> dupas;

    public Set<Dupa> getDupas() {
        if (dupas == null) dupas = new HashSet<>();
        return dupas;
    }

    public void setDupas(Set<Dupa> dupas) {
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
