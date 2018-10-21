CREATE TABLE Client (
  entity_id  serial       NOT NULL,
  first_name VARCHAR(255) NOT NULL,
  last_name  VARCHAR(255) NOT NULL,
  email      VARCHAR(255) NOT NULL UNIQUE,
  phone      VARCHAR(255) NOT NULL UNIQUE,
  active     BOOLEAN      NOT NULL,
  CONSTRAINT Client_pk PRIMARY KEY (entity_id)
) WITH (
OIDS = FALSE
);
