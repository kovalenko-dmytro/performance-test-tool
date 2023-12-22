CREATE TABLE public."files" (
    file_        char(36) DEFAULT uuid_generate_v4() NOT NULL,
    file_name    varchar(500)                        NOT NULL,
    content_type varchar(500)                        NOT NULL,
    file_size    bigint                              NOT NULL,
    bytes        bytea                               NOT NULL,
    created      timestamp                           NOT NULL,
    CONSTRAINT files_pkey PRIMARY KEY (file_)
);