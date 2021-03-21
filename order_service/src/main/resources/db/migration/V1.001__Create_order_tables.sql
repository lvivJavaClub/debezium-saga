CREATE TABLE cafe_order
(
    order_id   uuid PRIMARY KEY,
    customer_id uuid NOT NULL,
    currency_code varchar(3) NOT NULL,
    total_price int8 NOT NULL,
    status     varchar(50) NOT NULL,
    created_at timestamp   NOT NULL default now(),
    updated_at timestamp   NOT NULL default now()
);

CREATE TABLE cafe_order_item
(
    order_ref   uuid        NOT NULL,
    item_id     uuid        NOT NULL,
    product_sku VARCHAR(20) NOT NULL,
    quantity    integer CHECK ( quantity > 0 ),
    price       int8 NOT NULL,
    CONSTRAINT fk_cafe_order_item FOREIGN KEY (order_ref) REFERENCES cafe_order (order_id) ON DELETE SET NULL
);

