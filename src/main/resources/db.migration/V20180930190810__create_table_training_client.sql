CREATE TABLE training_client (
  training_id integer NOT NULL,
  client_id   integer NOT NULL,
  CONSTRAINT training_client_pk PRIMARY KEY (training_id)
) WITH (
OIDS = FALSE
);

ALTER TABLE training_client
  ADD CONSTRAINT training_client_fk_training_id
FOREIGN KEY (training_id) REFERENCES Training (entity_id);

ALTER TABLE training_client
  ADD CONSTRAINT training_client_fk_client_id
FOREIGN KEY (client_id) REFERENCES Client (entity_id);
