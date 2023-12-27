package com.gmail.apachdima.ptt.execution.model;

import com.gmail.apachdima.ptt.common.constant.execution.TestExecutionStatus;
import com.gmail.apachdima.ptt.file.storage.model.StoredFile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "test_executions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TestExecution {

    @Id
    @Column(name = "execution_", columnDefinition = "bpchar", unique = true, nullable = false)
    private String executionId;

    @Column(name = "executed_by", nullable = false)
    private String executedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TestExecutionStatus status;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "finished_at", nullable = false)
    private LocalDateTime finishedAt;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "log_", referencedColumnName = "file_")
    private StoredFile log;

    @ManyToOne
    @JoinColumn(name = "simulation_")
    private Simulation simulation;
}
