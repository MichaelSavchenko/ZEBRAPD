CREATE TABLE Trainer (
  entity_id      serial       NOT NULL,
  first_name     VARCHAR(255) NOT NULL,
  last_name      VARCHAR(255) NOT NULL,
  email          VARCHAR(255) NOT NULL,
  phone          VARCHAR(255) NOT NULL,
  active         BOOLEAN      NOT NULL,
  default_salary INTEGER      NOT NULL,
  CONSTRAINT Trainer_pk PRIMARY KEY (entity_id)
) WITH (
OIDS = FALSE
);