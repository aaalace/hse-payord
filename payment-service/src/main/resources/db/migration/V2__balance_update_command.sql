CREATE TABLE update_balance_command
(
    id      UUID         NOT NULL,
    user_id VARCHAR(255),
    amount  DECIMAL,
    state   VARCHAR(255) NOT NULL,
    type    VARCHAR(255),
    CONSTRAINT pk_update_balance_command PRIMARY KEY (id)
);