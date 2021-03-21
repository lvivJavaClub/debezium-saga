CREATE TABLE saga (
    id uuid PRIMARY KEY ,
    aggregate_type varchar(100) NOT NULL,
    aggregate_id varchar(36) NOT NULL,
    payload text,
    type varchar(100)
);