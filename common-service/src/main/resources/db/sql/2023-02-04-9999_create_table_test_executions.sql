CREATE TABLE public."test_executions" (
    execution_        char(36) DEFAULT uuid_generate_v4() NOT NULL,
    executed_by       varchar(255)                        NOT NULL,
    status            varchar(50)                         NOT NULL,
    started_at        timestamp                           NOT NULL,
    finished_at       timestamp                           NOT NULL,
    log_              char(36)                            NOT NULL,
    simulation_       char(36)                            NOT NULL,
    CONSTRAINT test_executions_pkey PRIMARY KEY (execution_),
    CONSTRAINT test_executions_log_fkey FOREIGN KEY (log_) REFERENCES public."files" (file_),
    CONSTRAINT test_executions_simulation_fkey FOREIGN KEY (simulation_) REFERENCES public."simulations" (simulation_) ON DELETE CASCADE
);