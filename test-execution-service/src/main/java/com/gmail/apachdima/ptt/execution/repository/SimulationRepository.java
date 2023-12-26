package com.gmail.apachdima.ptt.execution.repository;

import com.gmail.apachdima.ptt.execution.model.Simulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulationRepository extends JpaRepository<Simulation, String> {
}
