-- Hasło do każdego konta: password? --
INSERT INTO Account (version, login, first_name, last_name, email, password, active, confirmed)
VALUES (1, 'admin', 'Kazimierz', 'Andrzejewski', 'nieistnieje@aaa.pl', '0885c94ed2e94369bafab182efc19c41624dd8f2b12b79fdfd1c4e8a740397f6335cd241a1713b030476a31ec049ef2c5ec0f8ea3baa22246815bb5ccc74f01c', true, true),
       (1, 'pracownik', 'Eustachy', 'Kaczmarczyk', 'zmyslony@aaa.pl', '0885c94ed2e94369bafab182efc19c41624dd8f2b12b79fdfd1c4e8a740397f6335cd241a1713b030476a31ec049ef2c5ec0f8ea3baa22246815bb5ccc74f01c', true, true),
       (1, 'adminpracownik', 'Jadwiga', 'Sokołowska', 'zmyslony2@aaa.pl', '0885c94ed2e94369bafab182efc19c41624dd8f2b12b79fdfd1c4e8a740397f6335cd241a1713b030476a31ec049ef2c5ec0f8ea3baa22246815bb5ccc74f01c', true, true),
       (1, 'klient1', 'Patryk', 'Rutkowski', 'zmyslony3@aaa.pl', '0885c94ed2e94369bafab182efc19c41624dd8f2b12b79fdfd1c4e8a740397f6335cd241a1713b030476a31ec049ef2c5ec0f8ea3baa22246815bb5ccc74f01c', true, true),
       (1, 'klient2', 'Gabriela', 'Górska', 'zmyslony4@aaa.pl', '0885c94ed2e94369bafab182efc19c41624dd8f2b12b79fdfd1c4e8a740397f6335cd241a1713b030476a31ec049ef2c5ec0f8ea3baa22246815bb5ccc74f01c', true, true),
       (1, 'klient3', 'Anastazy', 'Cieślak', 'zmyslony5@aaa.pl', '0885c94ed2e94369bafab182efc19c41624dd8f2b12b79fdfd1c4e8a740397f6335cd241a1713b030476a31ec049ef2c5ec0f8ea3baa22246815bb5ccc74f01c', true, true);

INSERT INTO Access_level (version, level, account_id, active)
VALUES (1, 'ADMIN', 1, true),
       (1, 'EMPLOYEE', 2, true),
       (1, 'ADMIN', 3, true),
       (1, 'EMPLOYEE', 3, true),
       (1, 'CLIENT', 4, true),
       (1, 'CLIENT', 5, true),
       (1, 'CLIENT', 6, true);
