CREATE TABLE expired_access_tokens (
    jti_claim_id VARCHAR(255) NOT NULL,
    expiry_time TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT pk_exp_tokens PRIMARY KEY (jti_claim_id)
);

CREATE TABLE login_times (
    id VARCHAR(32) NOT NULL,
    user_id VARCHAR(32) NOT NULL,
    login_time TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT pk_login_times PRIMARY KEY (id),
    CONSTRAINT fk_login_times_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE logout_times (
    id VARCHAR(32) NOT NULL,
    user_id VARCHAR(32) NOT NULL,
    logout_time TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT pk_logout_times PRIMARY KEY (id),
    CONSTRAINT fk_logout_times_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE refresh_times (
    id VARCHAR(32) NOT NULL,
    user_id VARCHAR(32) NOT NULL,
    refresh_time TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT pk_refresh_times PRIMARY KEY (id),
    CONSTRAINT fk_refresh_times_user FOREIGN KEY (user_id) REFERENCES users(id)
);