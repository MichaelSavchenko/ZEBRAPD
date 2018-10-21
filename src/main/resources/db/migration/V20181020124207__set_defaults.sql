INSERT INTO training_price
VALUES ('INDIVIDUAL', 200),
       ('INDIVIDUAL_2', 350),
       ('INDIVIDUAL_3', 450),
       ('STRETCHING', 80),
       ('STRETCHING_NOT_FULL', 60),
       ('POLE_DANCE', 80),
       ('POLE_DANCE_NOT_FULL', 60);

INSERT INTO client
VALUES (1, 'client1', 'Client1', 'client1@gmail.com', '+380630000001', true),
       (2, 'client2', 'Client2', 'client2@gmail.com', '+380630000002', true),
       (3, 'client3', 'Client3', 'client3@gmail.com', '+380630000003', true),
       (4, 'client4', 'Client4', 'client4@gmail.com', '+380630000004', true),
       (5, 'client5', 'Client5', 'client5@gmail.com', '+380630000005', true),
       (6, 'client6', 'Client6', 'client6@gmail.com', '+380630000006', true),
       (7, 'client7', 'Client7', 'client7@gmail.com', '+380630000007', true);

INSERT INTO trainer
VALUES (1, 'trainer1', 'Trainer1', 'trainer1@gmail.com', '+380631000001', true, 0),
       (2, 'trainer2', 'Trainer2', 'trainer2@gmail.com', '+380631000002', true, 0),
       (3, 'trainer3', 'Trainer3', 'trainer3@gmail.com', '+380631000003', true, 0);

INSERT INTO subscription
VALUES (1, '2018-10-21', 1, 'PD', 10, 200, '2018-10-01', '2018-10-30'),
       (2, '2018-10-21', 2, 'PD', 10, 200, '2018-10-01', '2018-10-30'),
       (3, '2018-10-21', 3, 'PD', 10, 200, '2018-10-01', '2018-10-30'),
       (4, '2018-10-21', 4, 'PD', 10, 200, '2018-10-01', '2018-10-30'),
       (5, '2018-10-21', 5, 'PD', 10, 200, '2018-10-01', '2018-10-30'),
       (6, '2018-10-21', 6, 'PD', 10, 200, '2018-10-01', '2018-10-30'),
       (7, '2018-10-21', 7, 'PD', 10, 200, '2018-10-01', '2018-10-30'),
       (8, '2018-10-21', 4, 'ST', 10, 180, '2018-10-01', '2018-10-30'),
       (9, '2018-10-21', 1, 'ST', 10, 180, '2018-10-01', '2018-10-30'),
       (10, '2018-10-21', 2, 'IN', 10, 500, '2018-10-01', '2018-10-30'),
       (11, '2018-10-21', 2, 'IN_2', 10, 600, '2018-10-01', '2018-10-30');



