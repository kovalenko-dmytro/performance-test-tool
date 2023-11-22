CREATE TABLE public.users_roles (
    user_role_ char(36) DEFAULT uuid_generate_v4() NOT NULL,
    user_      char(36)                            NOT NULL,
    role_      char(36)                            NOT NULL,
    CONSTRAINT users_roles_pkey PRIMARY KEY (user_role_),
    CONSTRAINT users_roles_uk UNIQUE (user_, role_),
    CONSTRAINT users_roles_user_fkey FOREIGN KEY (user_) REFERENCES public."users" (user_),
    CONSTRAINT users_roles_role_fkey FOREIGN KEY (role_) REFERENCES public."roles" (role_)
);