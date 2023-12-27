package com.gmail.apachdima.ptt.execution.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "simulations")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Simulation {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "simulation_", columnDefinition = "bpchar", unique = true, nullable = false)
    private String simulationId;

    @Column(name = "simulation_class", nullable = false)
    private String simulationClass;

    @Column(name = "added_at", nullable = false)
    private LocalDateTime addedAt;

    @OneToMany(mappedBy = "simulation", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestExecution> testExecutions;
}
