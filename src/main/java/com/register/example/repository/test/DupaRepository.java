package com.register.example.repository.test;


import com.register.example.entity.test.Dupa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DupaRepository extends JpaRepository<Dupa, Long> {
    @Transactional
    @Modifying
    @Query("delete from Dupa u where u.id = ?1")
    Integer deleteDupaByID(Long id);

    @Transactional
    @Modifying
    @Query("delete from Dupa u where u.test.id = ?1")
    Integer deleteDupaByTestId(Long id);

}
