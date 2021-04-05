-- Hasło do każdego konta: password? --
INSERT INTO Account (version, login, password, active, confirmed)
VALUES (1, 'admin', '0885c94ed2e94369bafab182efc19c41624dd8f2b12b79fdfd1c4e8a740397f6335cd241a1713b030476a31ec049ef2c5ec0f8ea3baa22246815bb5ccc74f01c', true, true),
       (1, 'pracownik','0885c94ed2e94369bafab182efc19c41624dd8f2b12b79fdfd1c4e8a740397f6335cd241a1713b030476a31ec049ef2c5ec0f8ea3baa22246815bb5ccc74f01c', true, true),
       (1, 'adminpracownik', '0885c94ed2e94369bafab182efc19c41624dd8f2b12b79fdfd1c4e8a740397f6335cd241a1713b030476a31ec049ef2c5ec0f8ea3baa22246815bb5ccc74f01c', true, true),
       (1, 'klient1', '0885c94ed2e94369bafab182efc19c41624dd8f2b12b79fdfd1c4e8a740397f6335cd241a1713b030476a31ec049ef2c5ec0f8ea3baa22246815bb5ccc74f01c', true, true),
       (1, 'klient2', '0885c94ed2e94369bafab182efc19c41624dd8f2b12b79fdfd1c4e8a740397f6335cd241a1713b030476a31ec049ef2c5ec0f8ea3baa22246815bb5ccc74f01c', true, true),
       (1, 'klient3', '0885c94ed2e94369bafab182efc19c41624dd8f2b12b79fdfd1c4e8a740397f6335cd241a1713b030476a31ec049ef2c5ec0f8ea3baa22246815bb5ccc74f01c', true, true);

INSERT INTO Personal_data (id, version, first_name, last_name, email)
VALUES (1, 1, 'Kazimierz', 'Andrzejewski', 'nieistnieje@aaa.pl'),
       (2, 1, 'Eustachy', 'Kaczmarczyk', 'zmyslony@aaa.pl'),
       (3, 1, 'Jadwiga', 'Sokołowska', 'zmyslony2@aaa.pl'),
       (4, 1, 'Patryk', 'Rutkowski', 'zmyslony3@aaa.pl'),
       (5, 1, 'Gabriela', 'Górska', 'zmyslony4@aaa.pl'),
       (6, 1, 'Anastazy', 'Cieślak', 'zmyslony5@aaa.pl');

INSERT INTO Access_level (version, level, account_id, active)
VALUES (1, 'ADMIN', 1, true),
       (1, 'EMPLOYEE', 2, true),
       (1, 'ADMIN', 3, true),
       (1, 'EMPLOYEE', 3, true),
       (1, 'CLIENT', 4, true),
       (1, 'CLIENT', 5, true),
       (1, 'CLIENT', 6, true);

INSERT INTO Client_data (id, version, phone_number)
VALUES (5, 1, '48123456789'),
       (6, 1, '48987654321'),
       (7, 1, '48111111111');

INSERT INTO Ferry (version, name, vehicle_capacity, on_deck_capacity)
VALUES (1, 'Ever Given', 10, 100),
       (1, 'Fast one', 5, 50),
       (1, 'Black perl', 15, 200),
       (1, 'White perl', 20, 150);

INSERT INTO Seaport (version, city)
VALUES (1, 'Venice'),
       (1, 'Valencia'),
       (1, 'Barcelona'),
       (1, 'Alexandria');

INSERT INTO Route (version, start, destination)
VALUES (1, 1, 2),
       (1, 2, 1),
       (1, 3, 1),
       (1, 1, 3),
       (1, 2, 3),
       (1, 3, 2);

INSERT INTO Cabin_type (cabin_type_name)
VALUES ('First class'),
       ('Second class'),
       ('Third class'),
       ('Disabled class');

INSERT INTO Cabin (version, ferry, capacity, cabin_type)
VALUES (1, 1, 4, 1),
       (1, 1, 5, 2),
       (1, 1, 2, 1),
       (1, 1, 6, 3),
       (1, 2, 5, 2),
       (1, 2, 2, 1),
       (1, 2, 7, 3),
       (1, 3, 5, 1),
       (1, 3, 4, 2),
       (1, 3, 7, 3),
       (1, 3, 8, 3);

INSERT INTO Cruise (version, start_date, end_date, route, ferry)
VALUES (1, '2021-05-01 11:00:00', '2021-05-01 15:00:00', 1, 1),
       (1, '2021-05-01 15:00:00', '2021-05-01 20:00:00', 2, 2),
       (1, '2021-05-02 12:00:00', '2021-05-02 17:00:00', 3, 3),
       (1, '2021-05-03 15:00:00', '2021-05-03 21:00:00', 3, 3);


INSERT INTO Vehicle_type (vehicle_type_name, required_space)
VALUES ('None', 0),
       ('Motorcycle', 0.5),
       ('Car', 1),
       ('Bus', 2.5);

INSERT INTO Booking (version, cruise, account, number_of_people, cabin, vehicle_type, price)
VALUES (1, 1, 4, 5, 2, 1, 100),
       (1, 2, 5, 2, 6, 2, 200),
       (1, 3, 6, 2, 9, 3, 300);
