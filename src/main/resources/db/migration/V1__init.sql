CREATE TABLE users (
    id VARCHAR(32) NOT NULL,
    username VARCHAR(256) NOT NULL,
    password VARCHAR(256) NOT NULL,

    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uq_users_username UNIQUE (username)
);

CREATE TABLE roles (
    id VARCHAR(32) NOT NULL,
    name VARCHAR(256) NOT NULL,

    CONSTRAINT pk_roles PRIMARY KEY (id),
    CONSTRAINT uq_roles_name UNIQUE (name)
);

CREATE TABLE user_roles (
    id VARCHAR(32) NOT NULL,
    user_id VARCHAR(32) NOT NULL,
    role_id VARCHAR(32) NOT NULL,

    CONSTRAINT pk_user_roles PRIMARY KEY (id),

    CONSTRAINT uq_user_role UNIQUE (user_id, role_id),

    CONSTRAINT fk_user_roles_user
        FOREIGN KEY(user_id)
            REFERENCES users(id)
            ON DELETE RESTRICT,

    CONSTRAINT fk_user_roles_role
        FOREIGN KEY(role_id)
            REFERENCES roles(id)
            ON DELETE RESTRICT
);