CREATE TABLE public."simulations" (
    simulation_       char(36) DEFAULT uuid_generate_v4()                                           NOT NULL,
    simulation_class  varchar(1000)                                                                 NOT NULL,
    added_at          timestamp without time zone DEFAULT (now()):: timestamp (0) without time zone NOT NULL,
    CONSTRAINT simulations_pkey PRIMARY KEY (simulation_),
    CONSTRAINT simulations_simulation_class_uk UNIQUE (simulation_class)
);