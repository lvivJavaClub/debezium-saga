CREATE TABLE payment_order (
    payment_id uuid PRIMARY KEY,
    order_id uuid NOT NULL,
    customer_id uuid NOT NULL,
    currency_code VARCHAR(3) NOT NULL,
    amount int8 NOT NULL,
    payment_status varchar(20) NOT NULL,
    created_at timestamp,
    updated_at timestamp
);