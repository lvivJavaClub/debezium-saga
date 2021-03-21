CREATE TABLE delivery_order
(
    order_id    UUID primary key,
    status      varchar(50) not null,
    customer_id uuid        not null,
    agent_ref   uuid        not null,
    created_at  timestamp,
    updated_at  timestamp,
    Constraint fk_delivery_order_agent FOREIGN KEY (agent_ref)
        references delivery_agent (agent_id) ON DELETE SET NULL
);