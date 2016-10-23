package com.register.example.repository.test;


import com.register.example.entity.test.Upa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpaRepository extends JpaRepository<Upa, Long> {

}
