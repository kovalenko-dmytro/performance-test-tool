CREATE TABLE public."files" (
    file_        char(36) DEFAULT uuid_generate_v4() NOT NULL,
    storage_id   varchar(255)                        NOT NULL,
    file_name    varchar(500)                        NOT NULL,
    content_type varchar(50)                         NOT NULL,
    file_size    bigint                              NOT NULL,
    created      timestamp                           NOT NULL,
    CONSTRAINT files_pkey PRIMARY KEY (file_),
    CONSTRAINT files_storage_id_uk UNIQUE (storage_id)
);