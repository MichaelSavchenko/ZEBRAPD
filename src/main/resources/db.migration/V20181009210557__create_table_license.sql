CREATE TABLE License (
  entity_id           serial       NOT NULL,
  client_id           integer      NOT NULL,
  type                VARCHAR(20) NOT NULL,
  number_of_trainings integer      NOT NULL,
  price               integer      NOT NULL,
  start_date          TIME         NOT NULL,
  expiration_date     TIME         NOT NULL,
  CONSTRAINT License_pk PRIMARY KEY (entity_id)
) WITH (
OIDS = FALSE
);

ALTER TABLE License
  ADD CONSTRAINT License_fk_client_id FOREIGN KEY (client_id) REFERENCES Client (entity_id);