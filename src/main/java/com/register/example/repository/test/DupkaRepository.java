package com.register.example.repository.test;


import com.register.example.entity.test.Dupka;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DupkaRepository extends JpaRepository<Dupka, Long> {

}
