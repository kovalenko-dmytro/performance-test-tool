CREATE TABLE public."roles" (
    role_ char(36) DEFAULT uuid_generate_v4() NOT NULL,
    role  varchar(255)                        NOT NULL,
    CONSTRAINT roles_pkey PRIMARY KEY (role_),
    CONSTRAINT roles_role_uk UNIQUE (role)
);