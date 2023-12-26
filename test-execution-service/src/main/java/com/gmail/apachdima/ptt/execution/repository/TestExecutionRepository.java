package com.gmail.apachdima.ptt.execution.repository;

import com.gmail.apachdima.ptt.execution.model.TestExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestExecutionRepository extends JpaRepository<TestExecution, String> {
}
