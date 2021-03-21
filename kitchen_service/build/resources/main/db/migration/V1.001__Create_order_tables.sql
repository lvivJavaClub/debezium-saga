CREATE TABLE kitchen_order
(
    kitchen_order_id uuid PRIMARY KEY,
    order_id   uuid,
    status     varchar(50) NOT NULL,
    created_at timestamp   NOT NULL default now(),
    updated_at timestamp   NOT NULL default now()
);

