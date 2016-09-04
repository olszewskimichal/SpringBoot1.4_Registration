package com.register.example.repository.test;


import com.register.example.entity.test.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {

    @Query("select distinct u from Test u join fetch u.dupas")
    Set<Test> findALL();

    @Transactional
    @Modifying
    @Query("delete from Test u where u.id = ?1")
    Integer deleteTestByID(Long id);

    @Query("select distinct u from Test u join fetch u.dupas where u.id=?1")
    Test findByID(Long id);
}
