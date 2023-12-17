CREATE TABLE public."email_logs" (
    email_        char(36) DEFAULT uuid_generate_v4() NOT NULL,
    send_by       varchar(255)                        NOT NULL,
    send_to       varchar(255)                        NOT NULL,
    email_type    varchar(50)                         NOT NULL,
    email_status  varchar(50)                         NOT NULL,
    send_time     timestamp                           NOT NULL,
    CONSTRAINT email_logs_pkey PRIMARY KEY (email_)
);