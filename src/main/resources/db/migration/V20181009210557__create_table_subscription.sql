CREATE TABLE Subscription (
  entity_id           serial      NOT NULL,
  date_of_sale        Date        NOT NULL,
  client_id           integer     NOT NULL,
  type                varchar(20) NOT NULL,
  number_of_trainings integer     NOT NULL,
  price               integer     NOT NULL,
  start_date          Date        NOT NULL,
  expiration_date     Date        NOT NULL,
  CONSTRAINT Subscription_pk PRIMARY KEY (entity_id)
) WITH (
OIDS = FALSE
);

ALTER TABLE Subscription
  ADD CONSTRAINT subscription_fk_client_id FOREIGN KEY (client_id) REFERENCES Client (entity_id);