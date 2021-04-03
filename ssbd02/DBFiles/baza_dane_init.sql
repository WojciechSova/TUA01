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
