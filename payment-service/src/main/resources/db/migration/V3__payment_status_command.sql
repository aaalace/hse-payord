CREATE TABLE payment_status_command
(
    id                        UUID         NOT NULL,
    user_id                   VARCHAR(255),
    order_id                  VARCHAR(255),
    payment_status            VARCHAR(255),
    state                     VARCHAR(255) NOT NULL,
    balance_update_command_id UUID,
    CONSTRAINT pk_payment_status_command PRIMARY KEY (id)
);