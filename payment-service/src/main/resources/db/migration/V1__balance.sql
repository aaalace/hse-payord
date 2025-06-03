CREATE TABLE balance
(
    id      UUID NOT NULL,
    user_id VARCHAR(255),
    balance DECIMAL,
    CONSTRAINT pk_balance PRIMARY KEY (id)
);