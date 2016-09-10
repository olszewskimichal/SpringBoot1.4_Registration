package com.register.example.repository;

import com.register.example.soap.WebServiceOperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationLogRepository extends JpaRepository<WebServiceOperationLog,Long> {
}
