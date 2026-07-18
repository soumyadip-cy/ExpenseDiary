CREATE TABLE beneficiaries (
    id VARCHAR(32) NOT NULL,
    name VARCHAR(250) NOT NULL,
    description VARCHAR(1000),
    address VARCHAR(250),
    phone VARCHAR(20),
    email VARCHAR(200),
    beneficiary_is_active BOOLEAN NOT NULL DEFAULT TRUE,
    activation_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deactivation_time TIMESTAMP WITH TIME ZONE,

    CONSTRAINT pk_beneficiaries PRIMARY KEY (id),
    CONSTRAINT uq_beneficiaries_name UNIQUE (name)
);

CREATE TABLE merchants (
    id VARCHAR(32) NOT NULL,
    name VARCHAR(250) NOT NULL,
    description VARCHAR(1000),
    address VARCHAR(250),
    phone VARCHAR(20),
    email VARCHAR(200),
    merchant_is_active BOOLEAN NOT NULL DEFAULT TRUE,
    activation_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deactivation_time TIMESTAMP WITH TIME ZONE,

    CONSTRAINT pk_merchants PRIMARY KEY (id),
    CONSTRAINT uq_merchants_name UNIQUE (name)
);

CREATE TABLE transaction_types (
    id VARCHAR(32) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(1000)
)

CREATE TABLE transactions (
    id VARCHAR(32) NOT NULL,
    transaction_time TIMESTAMP WITH TIME ZONE NOT NULL,
    creation_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    merchant_id VARCHAR(32) NOT NULL,
    beneficiary_id VARCHAR(32) NOT NULL,
    type VARCHAR(32) NOT NULL,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(1000),
    amount DOUBLE(15,2) NOT NULL,

    CONSTRAINT pk_transactions PRIMARY KEY (id),
    CONSTRAINT fk_transactions_merchant
        FOREIGN KEY (merchant_id)
        REFERENCES merchants (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_transactions_beneficiary
        FOREIGN KEY (beneficiary_id)
        REFERENCES beneficiaries (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_transactions_type
        FOREIGN KEY (type)
        REFERENCES transaction_types (id)
        ON DELETE CASCADE
);

CREATE INDEX ix_transactions_merchants
    ON transactions (merchant_id);

CREATE INDEX ix_transactions_beneficiaries
    ON transactions (beneficiary_id);

CREATE INDEX ix_transactions_time
    ON transactions (transaction_time);

CREATE INDEX ix_transaction_amount
    ON transactions (amount);