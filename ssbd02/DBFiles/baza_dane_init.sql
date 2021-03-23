INSERT INTO Account (version, login, name, surname, email, phone_number, password, active, confirmed) 
VALUES (1, 'CzlowiekOdBazy', 'Wojciech', 'Sowa', 'nieistnieje@aaa.pl', '+48123456789', '0885c94ed2e94369bafab182efc19c41624dd8f2b12b79fdfd1c4e8a740397f6335cd241a1713b030476a31ec049ef2c5ec0f8ea3baa22246815bb5ccc74f01c', true, true);


INSERT INTO Access_level (version, level, account_id, active)
VALUES  (1, 'ADMIN', 1, true),
		(1, 'CLIENT', 1, true),
		(1, 'EMPLOYEE', 1, false);

