-- Hasło do każdego konta: password? --
INSERT INTO Account (version, login, password, active, confirmed)
VALUES (1, 'admin', '0885c94ed2e94369bafab182efc19c41624dd8f2b12b79fdfd1c4e8a740397f6335cd241a1713b030476a31ec049ef2c5ec0f8ea3baa22246815bb5ccc74f01c', true, true),
       (1, 'pracownik','0885c94ed2e94369bafab182efc19c41624dd8f2b12b79fdfd1c4e8a740397f6335cd241a1713b030476a31ec049ef2c5ec0f8ea3baa22246815bb5ccc74f01c', true, true),
       (1, 'adminpracownik', '0885c94ed2e94369bafab182efc19c41624dd8f2b12b79fdfd1c4e8a740397f6335cd241a1713b030476a31ec049ef2c5ec0f8ea3baa22246815bb5ccc74f01c', true, true),
       (1, 'klient1', '0885c94ed2e94369bafab182efc19c41624dd8f2b12b79fdfd1c4e8a740397f6335cd241a1713b030476a31ec049ef2c5ec0f8ea3baa22246815bb5ccc74f01c', true, true),
       (1, 'klient2', '0885c94ed2e94369bafab182efc19c41624dd8f2b12b79fdfd1c4e8a740397f6335cd241a1713b030476a31ec049ef2c5ec0f8ea3baa22246815bb5ccc74f01c', true, true),
       (1, 'klient3', '0885c94ed2e94369bafab182efc19c41624dd8f2b12b79fdfd1c4e8a740397f6335cd241a1713b030476a31ec049ef2c5ec0f8ea3baa22246815bb5ccc74f01c', true, true);

INSERT INTO Personal_data (id, first_name, last_name, email, language, time_zone)
VALUES (1, 'Kazimierz', 'Andrzejewski', 'nieistnieje@aaa.pl', 'pl', '+01:00'),
       (2, 'Eustachy', 'Kaczmarczyk', 'zmyslony@aaa.pl', 'en', '+01:00'),
       (3, 'Jadwiga', 'Sokołowska', 'zmyslony2@aaa.pl', 'en', '+01:00');

INSERT INTO Personal_data (id, first_name, last_name, email, phone_number, language, time_zone)
VALUES (4, 'Patryk', 'Rutkowski', 'zmyslony3@aaa.pl', '48123456789', 'pl', '+01:00'),
       (5, 'Gabriela', 'Górska', 'zmyslony4@aaa.pl', '48987654321', 'en', '+01:00'),
       (6, 'Anastazy', 'Cieślak', 'zmyslony5@aaa.pl', '48111111111', 'pl', '+01:00');

INSERT INTO Access_level (version, level, account, active)
VALUES (1, 'ADMIN', 1, true),
       (1, 'EMPLOYEE', 2, true),
       (1, 'ADMIN', 3, true),
       (1, 'EMPLOYEE', 3, true),
       (1, 'CLIENT', 4, true),
       (1, 'CLIENT', 5, true),
       (1, 'CLIENT', 6, true);

INSERT INTO Ferry (version, name, vehicle_capacity, on_deck_capacity)
VALUES (1, 'Ever Given', 10, 100),
       (1, 'Fast one', 5, 50),
       (1, 'Black perl', 15, 200),
       (1, 'White perl', 20, 150);

INSERT INTO Seaport (version, city, code)
VALUES (1, 'Venice', 'VEN'),
       (1, 'Valencia', 'VAL'),
       (1, 'Barcelona', 'BAR'),
       (1, 'Alexandria', 'ALE');

INSERT INTO Route (version, start, destination, code)
VALUES (1, 1, 2, 'VENVAL'),
       (1, 2, 1, 'VALVEN'),
       (1, 3, 1, 'BARVEN'),
       (1, 1, 3, 'VENBAR'),
       (1, 2, 3, 'VALBAR'),
       (1, 3, 2, 'BARVAL');

INSERT INTO Cabin_type (cabin_type_name)
VALUES ('First class'),
       ('Second class'),
       ('Third class'),
       ('Disabled class');

INSERT INTO Cabin (version, ferry, capacity, cabin_type, number)
VALUES (1, 1, 4, 1, 'A321'),
       (1, 1, 5, 2, 'A101'),
       (1, 1, 2, 1, 'A203'),
       (1, 1, 6, 3, 'A222'),
       (1, 2, 5, 2, 'H666'),
       (1, 2, 2, 1, 'B345'),
       (1, 2, 7, 3, 'B137'),
       (1, 3, 5, 1, 'C128'),
       (1, 3, 4, 2, 'C655'),
       (1, 3, 7, 3, 'G234'),
       (1, 3, 8, 3, 'G123');

INSERT INTO Cruise (version, start_date, end_date, route, ferry, number, created_by)
VALUES (1, '2021-05-01 11:00:00', '2021-05-01 15:00:00', 1, 1, 'VENVAL000001', 2),
       (1, '2021-05-01 15:00:00', '2021-05-01 20:00:00', 2, 2, 'VALVEN000001', 2),
       (1, '2021-05-02 12:00:00', '2021-05-02 17:00:00', 3, 3, 'BARVEN000001', 2),
       (1, '2021-05-03 15:00:00', '2021-05-03 21:00:00', 3, 3, 'BARVEN000002', 2);


INSERT INTO Vehicle_type (vehicle_type_name, required_space)
VALUES ('None', 0),
       ('Motorcycle', 0.5),
       ('Car', 1),
       ('Bus', 2.5);

INSERT INTO Booking (version, cruise, account, number_of_people, cabin, vehicle_type, price, number)
VALUES (1, 1, 4, 5, 2, 1, 100, '0000000001'),
       (1, 2, 5, 2, 6, 2, 200, '0000000002'),
       (1, 3, 6, 2, 9, 3, 300, '0000000003');
