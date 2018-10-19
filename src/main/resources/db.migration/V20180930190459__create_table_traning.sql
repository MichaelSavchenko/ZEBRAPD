CREATE TABLE Training (
  entity_id  serial  NOT NULL,
  date_time  time    NOT NULL,
  trainer_id integer NOT NULL,
  type       varchar(20) NOT NULL,
  CONSTRAINT Training_pk PRIMARY KEY (entity_id)
) WITH (
OIDS = FALSE
);

ALTER TABLE Training
  ADD CONSTRAINT Training_fk_trainer_id FOREIGN KEY (trainer_id) REFERENCES Trainer (entity_id);
