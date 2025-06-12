CREATE TABLE payment_request_command
(
    id       VARCHAR(255) NOT NULL,
    order_id VARCHAR(255),
    user_id  VARCHAR(255),
    amount   DECIMAL,
    state    VARCHAR(255) NOT NULL,
    CONSTRAINT pk_payment_request_command PRIMARY KEY (id)
);