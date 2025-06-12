CREATE TABLE orders
(
    id      VARCHAR(255) NOT NULL,
    user_id VARCHAR(255),
    status  VARCHAR(255) NOT NULL,
    amount  DECIMAL,
    CONSTRAINT pk_orders PRIMARY KEY (id)
);