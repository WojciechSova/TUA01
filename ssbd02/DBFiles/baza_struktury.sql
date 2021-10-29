DROP TABLE IF EXISTS Booking;
DROP TABLE IF EXISTS Cabin;
DROP TABLE IF EXISTS Cruise;
DROP TABLE IF EXISTS Ferry;
DROP TABLE IF EXISTS Route;
DROP TABLE IF EXISTS Seaport;
DROP TABLE IF EXISTS Cabin_type;
DROP TABLE IF EXISTS Vehicle_type;
DROP TABLE IF EXISTS One_time_url;
DROP TABLE IF EXISTS Access_level;
DROP TABLE IF EXISTS Personal_data;
DROP TABLE IF EXISTS Account;
DROP VIEW IF EXISTS Auth_view;

CREATE USER IF NOT EXISTS 'ssbd02admin' IDENTIFIED BY 'admin_password';
CREATE USER IF NOT EXISTS 'ssbd02mok' IDENTIFIED BY 'mok_password';
CREATE USER IF NOT EXISTS 'ssbd02mop' IDENTIFIED BY 'mop_password';
CREATE USER IF NOT EXISTS 'ssbd02auth' IDENTIFIED BY 'auth_password';

CREATE TABLE Account
(
    id        bigint             NOT NULL AUTO_INCREMENT,
    version   bigint             NOT NULL,
    login     varchar(30)        NOT NULL,
    password  char(128)          NOT NULL,
    active    bool DEFAULT true  NOT NULL,
    confirmed bool DEFAULT false NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT login_unique UNIQUE (login)
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE Account TO 'ssbd02mok';
GRANT SELECT ON TABLE Account TO 'ssbd02mop';

CREATE INDEX account_login USING btree ON Account (login);

CREATE TABLE Personal_data
(
    id                          bigint                                             NOT NULL,
    first_name                  varchar(30)                                        NOT NULL,
    last_name                   varchar(50)                                        NOT NULL,
    email                       varchar(70)                                        NOT NULL,
    phone_number                varchar(15),
    language                    varchar(5)                                         NOT NULL,
    time_zone                   char(6)                                            NOT NULL,
    modification_date           timestamp,
    modified_by                 bigint,
    activity_modification_date  timestamp,
    activity_modified_by        bigint,
    confirmed_modification_date timestamp,
    password_modification_date  timestamp,
    email_modification_date     timestamp,
    creation_date               timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_known_good_login       timestamp,
    last_known_good_login_ip    varchar(39),
    last_known_bad_login        timestamp,
    last_known_bad_login_ip     varchar(39),
    number_of_bad_logins        int                      DEFAULT 0                 NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_personal_data_account_id_modified_by FOREIGN KEY (modified_by) REFERENCES Account (id),
    CONSTRAINT fk_account_id_activity_modified_by FOREIGN KEY (activity_modified_by) REFERENCES Account (id),
    CONSTRAINT fk_account_id_id FOREIGN KEY (id) REFERENCES Account (id),
    CONSTRAINT email_unique UNIQUE (email),
    CONSTRAINT phone_number_unique UNIQUE (phone_number)
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE Personal_data TO 'ssbd02mok';
GRANT SELECT ON TABLE Personal_data TO 'ssbd02mop';

CREATE INDEX personal_data_id USING btree ON Personal_data (id);
CREATE INDEX personal_data_modified_by USING btree ON Personal_data (modified_by);
CREATE INDEX personal_data_activity_modified_by USING btree ON Personal_data  (activity_modified_by);
CREATE INDEX personal_data_email USING btree ON Personal_data  (email);
CREATE INDEX personal_data_phone_number USING btree ON Personal_data  (phone_number);

CREATE TABLE Access_level
(
    id                bigint                                             NOT NULL AUTO_INCREMENT,
    version           bigint                                             NOT NULL,
    level             varchar(16)                                        NOT NULL,
    account           bigint                                             NOT NULL,
    active            boolean                  DEFAULT true              NOT NULL,
    modification_date timestamp,
    modified_by       bigint,
    creation_date     timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by        bigint,
    PRIMARY KEY (id),
    CONSTRAINT fk_account_id_modified_by FOREIGN KEY (modified_by) REFERENCES Account (id),
    CONSTRAINT fk_account_id_created_by FOREIGN KEY (created_by) REFERENCES Account (id),
    CONSTRAINT fk_account_id_account FOREIGN KEY (account) REFERENCES Account (id),
    CONSTRAINT account_level_unique UNIQUE (account, level)
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE Access_level TO 'ssbd02mok';
GRANT SELECT ON TABLE Access_level TO 'ssbd02mop';

CREATE INDEX access_level_account USING btree ON Access_level  (account);
CREATE INDEX access_level_modified_by USING btree ON Access_level  (modified_by);
CREATE INDEX access_level_created_by USING btree ON Access_level  (created_by);
CREATE INDEX access_level_level USING btree ON Access_level  (level);

CREATE VIEW Auth_view AS
SELECT Account.login, Account.password, Access_level.level
FROM (Account
         JOIN Access_level ON
    ((Account.id = Access_level.Account)))
WHERE (((Account.confirmed = true) AND (Account.active = true))
    AND (Access_level.active = true));

GRANT SELECT ON TABLE Auth_view TO 'ssbd02auth';

CREATE TABLE Ferry
(
    id                bigint                                             NOT NULL AUTO_INCREMENT,
    version           bigint                                             NOT NULL,
    name              varchar(30)                                        NOT NULL,
    vehicle_capacity  int                                                NOT NULL,
    on_deck_capacity  int                                                NOT NULL,
    modification_date timestamp,
    modified_by       bigint,
    creation_date     timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by        bigint,
    PRIMARY KEY (id),
    CONSTRAINT vehicle_capacity_not_negative CHECK (vehicle_capacity >= 0),
    CONSTRAINT on_deck_capacity_greater_than_zero CHECK (on_deck_capacity > 0),
    CONSTRAINT ferry_name_unique UNIQUE (name),
    CONSTRAINT fk_ferry_account_id_modified_by FOREIGN KEY (modified_by) REFERENCES Account (id),
    CONSTRAINT fk_ferry_account_id_created_by FOREIGN KEY (created_by) REFERENCES Account (id)
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE Ferry TO 'ssbd02mop';

CREATE INDEX ferry_modified_by USING btree ON Ferry  (modified_by);
CREATE INDEX ferry_created_by USING btree ON Ferry  (created_by);
CREATE INDEX ferry_name USING btree ON Ferry (name);

CREATE TABLE Seaport
(
    id                bigint                                             NOT NULL AUTO_INCREMENT,
    version           bigint                                             NOT NULL,
    city              varchar(30)                                        NOT NULL,
    code              varchar(3)                                         NOT NULL,
    modification_date timestamp,
    modified_by       bigint,
    creation_date     timestamp DEFAULT CURRENT_TIMESTAMP                NOT NULL,
    created_by        bigint,
    PRIMARY KEY (id),
    CONSTRAINT fk_seaport_account_id_modified_by FOREIGN KEY (modified_by) REFERENCES Account (id),
    CONSTRAINT fk_seaport_account_id_created_by FOREIGN KEY (created_by) REFERENCES Account (id),
    CONSTRAINT city_unique UNIQUE (city),
    CONSTRAINT seaport_code_unique UNIQUE (code)
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE Seaport TO 'ssbd02mop';

CREATE INDEX seaport_modified_by USING btree ON Seaport  (modified_by);
CREATE INDEX seaport_created_by USING btree ON Seaport  (created_by);
CREATE INDEX seaport_city USING btree ON Seaport  (city);
CREATE INDEX seaport_code USING btree ON Seaport  (code);

CREATE TABLE Route
(
    id            bigint                                             NOT NULL AUTO_INCREMENT,
    version       bigint                                             NOT NULL,
    start         bigint                                             NOT NULL,
    destination   bigint                                             NOT NULL,
    code          varchar(6)                                         NOT NULL,
    creation_date timestamp DEFAULT CURRENT_TIMESTAMP                NOT NULL,
    created_by    bigint,
    PRIMARY KEY (id),
    CONSTRAINT fk_seaport_id_start FOREIGN KEY (start) REFERENCES Seaport (id),
    CONSTRAINT fk_seaport_id_destination FOREIGN KEY (destination) REFERENCES Seaport (id),
    CONSTRAINT fk_route_account_id_created_by FOREIGN KEY (created_by) REFERENCES Account (id),
    CONSTRAINT start_destination_unique UNIQUE (start, destination),
    CONSTRAINT route_code_unique UNIQUE (code)
);

GRANT SELECT, INSERT, DELETE ON TABLE Route TO 'ssbd02mop';

CREATE INDEX route_created_by USING btree ON Route  (created_by);
CREATE INDEX route_start USING btree ON Route  (start);
CREATE INDEX route_destination USING btree ON Route  (destination);
CREATE INDEX route_code USING btree ON Route  (code);

CREATE TABLE Cabin_type
(
    id              bigint      NOT NULL AUTO_INCREMENT,
    cabin_type_name varchar(30) NOT NULL,
    PRIMARY KEY (id)
);

GRANT SELECT ON TABLE Cabin_type TO 'ssbd02mop';

CREATE TABLE Cabin
(
    id                bigint                                             NOT NULL AUTO_INCREMENT,
    version           bigint                                             NOT NULL,
    ferry             bigint                                             NOT NULL,
    capacity          int                                                NOT NULL,
    cabin_type        bigint                                             NOT NULL,
    number            varchar(4)                                         NOT NULL,
    modification_date timestamp,
    modified_by       bigint,
    creation_date     timestamp DEFAULT CURRENT_TIMESTAMP                NOT NULL,
    created_by        bigint,
    PRIMARY KEY (id),
    CONSTRAINT capacity_greater_than_zero CHECK (capacity > 0),
    CONSTRAINT fk_ferry_id FOREIGN KEY (ferry) REFERENCES Ferry (id),
    CONSTRAINT fk_cabin_type_id FOREIGN KEY (cabin_type) REFERENCES Cabin_type (id),
    CONSTRAINT fk_cabin_account_id_modified_by FOREIGN KEY (modified_by) REFERENCES Account (id),
    CONSTRAINT fk_cabin_account_id_created_by FOREIGN KEY (created_by) REFERENCES Account (id),
    CONSTRAINT cabin_ferry_number_unique UNIQUE (ferry, number)
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE Cabin TO 'ssbd02mop';

CREATE INDEX cabin_ferry USING btree ON Cabin  (ferry);
CREATE INDEX cabin_cabin_type USING btree ON Cabin  (cabin_type);
CREATE INDEX cabin_modified_by USING btree ON Cabin  (modified_by);
CREATE INDEX cabin_created_by USING btree ON Cabin  (created_by);
CREATE INDEX cabin_number USING btree ON Cabin  (number);

CREATE TABLE Cruise
(
    id                bigint                                             NOT NULL AUTO_INCREMENT,
    version           bigint                                             NOT NULL,
    start_date        timestamp                                          NOT NULL,
    end_date          timestamp                                          NOT NULL,
    route             bigint                                             NOT NULL,
    ferry             bigint                                             NOT NULL,
    number            varchar(12)                                        NOT NULL,
    popularity        float                                                NOT NULL,
    modification_date timestamp,
    modified_by       bigint,
    creation_date     timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by        bigint,
    PRIMARY KEY (id),
    CONSTRAINT end_date_after_start_date CHECK (end_date > start_date),
    CONSTRAINT fk_route_id FOREIGN KEY (route) REFERENCES Route (id),
    CONSTRAINT fk_cruise_ferry_id FOREIGN KEY (ferry) REFERENCES Ferry (id),
    CONSTRAINT fk_cruise_account_id_modified_by FOREIGN KEY (modified_by) REFERENCES Account (id),
    CONSTRAINT fk_cruise_account_id_created_by FOREIGN KEY (created_by) REFERENCES Account (id),
    CONSTRAINT cruise_number_unique UNIQUE (number)
);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE Cruise TO 'ssbd02mop';

CREATE INDEX cruise_ferry USING btree ON Cruise  (ferry);
CREATE INDEX cruise_route USING btree ON Cruise  (route);
CREATE INDEX cruise_modified_by USING btree ON Cruise  (modified_by);
CREATE INDEX cruise_created_by USING btree ON Cruise  (created_by);
CREATE INDEX cruise_number USING btree ON Cruise  (number);

CREATE TABLE Vehicle_type
(
    id                bigint      NOT NULL AUTO_INCREMENT,
    vehicle_type_name varchar(30) NOT NULL,
    required_space    float       NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT required_space_not_negative CHECK (required_space >= 0)
);

GRANT SELECT ON TABLE Vehicle_type TO 'ssbd02mop';

CREATE TABLE Booking
(
    id               bigint                                             NOT NULL AUTO_INCREMENT,
    version          bigint                                             NOT NULL,
    cruise           bigint                                             NOT NULL,
    account          bigint                                             NOT NULL,
    number_of_people int                                                NOT NULL,
    cabin            bigint,
    vehicle_type     bigint                                             NOT NULL,
    price            float                                              NOT NULL,
    number           varchar(10)                                        NOT NULL,
    creation_date    timestamp DEFAULT CURRENT_TIMESTAMP                NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT number_of_people_greater_than_zero CHECK (number_of_people > 0),
    CONSTRAINT price_greater_than_zero CHECK (price > 0),
    CONSTRAINT fk_cruise_id FOREIGN KEY (cruise) REFERENCES Cruise (id),
    CONSTRAINT fk_account_id FOREIGN KEY (account) REFERENCES Account (id),
    CONSTRAINT fk_cabin_id FOREIGN KEY (cabin) REFERENCES Cabin (id),
    CONSTRAINT fk_vehicle_type_id FOREIGN KEY (vehicle_type) REFERENCES Vehicle_type (id),
    CONSTRAINT booking_number_unique UNIQUE (number)
);

GRANT SELECT, INSERT, DELETE ON TABLE Booking TO 'ssbd02mop';

CREATE INDEX booking_cruise USING btree ON Booking  (cruise);
CREATE INDEX booking_account USING btree ON Booking  (account);
CREATE INDEX booking_cabin USING btree ON Booking  (cabin);
CREATE INDEX booking_vehicle_type USING btree ON Booking  (vehicle_type);
CREATE INDEX booking_number USING btree ON Booking  (number);

CREATE TABLE One_time_url
(
    id                bigint                                             NOT NULL AUTO_INCREMENT,
    version           bigint                                             NOT NULL,
    url               char(32)                                           NOT NULL,
    account           bigint                                             NOT NULL,
    action_type       varchar(6)                                         NOT NULL,
    new_email         varchar(70),
    expire_date       timestamp                                          NOT NULL,
    modification_date timestamp,
    modified_by       bigint,
    creation_date     timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by        bigint,
    PRIMARY KEY (id),
    CONSTRAINT fk_url_account_id_modified_by FOREIGN KEY (modified_by) REFERENCES Account (id),
    CONSTRAINT fk_url_account_id_created_by FOREIGN KEY (created_by) REFERENCES Account (id),
    CONSTRAINT fk_url_account_id FOREIGN KEY (account) REFERENCES Account (id),
    CONSTRAINT one_time_url_url_unique UNIQUE (url),
    CONSTRAINT new_email_unique UNIQUE (new_email),
    CONSTRAINT one_time_url_account_action_type_unique UNIQUE (account, action_type)
);

DELIMITER ;;

CREATE TRIGGER One_time_url_expire_date_new
    BEFORE INSERT ON One_time_url
    FOR EACH ROW
BEGIN
    IF NEW.expire_date <= CURRENT_TIMESTAMP THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Expire date is not from the future';
    END IF;
END;;

CREATE TRIGGER One_time_url_expire_date_modified
    AFTER UPDATE ON One_time_url
    FOR EACH ROW
BEGIN
    IF NEW.expire_date <= CURRENT_TIMESTAMP THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Expire date is not from the future';
    END IF;
END;;

DELIMITER ;


GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE One_time_url TO 'ssbd02mok';

CREATE INDEX one_time_url_url USING btree ON One_time_url  (url);
CREATE INDEX one_time_url_account USING btree ON One_time_url  (account);
CREATE INDEX one_time_url_modified_by USING btree ON One_time_url  (modified_by);
CREATE INDEX one_time_url_created_by USING btree ON One_time_url  (created_by);

GRANT ALL PRIVILEGES ON *.* TO 'ssbd02admin';

FLUSH PRIVILEGES;
