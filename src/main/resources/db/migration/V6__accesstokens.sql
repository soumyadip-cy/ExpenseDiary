CREATE TABLE access_times (
    id VARCHAR(32) NOT NULL,
    user_id VARCHAR(32) NOT NULL,
    type VARCHAR(10) NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT pk_access_times PRIMARY KEY (id),
    CONSTRAINT fk_access_times_user FOREIGN KEY (user_id) REFERENCES users(id)
);