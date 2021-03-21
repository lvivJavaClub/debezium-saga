CREATE TABLE delivery_agent (
    agent_id UUID PRIMARY KEY ,
    status varchar(50) NOT NULL,
    created_at timestamp,
    updated_at timestamp
);

INSERT INTO delivery_agent VALUES ('ee37bbac-220f-413e-881d-f39c81470ecd', 'SLEEPING', now(), now());
INSERT INTO delivery_agent VALUES ('d70e3dc7-a31c-4a37-9f44-5afe5b699721', 'SLEEPING', now(), now());
