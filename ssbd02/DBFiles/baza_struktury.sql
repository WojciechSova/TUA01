DROP TABLE IF EXISTS Account CASCADE;
DROP TABLE IF EXISTS Access_level CASCADE;
DROP TABLE IF EXISTS Personal_data CASCADE;
DROP TABLE IF EXISTS Client_data CASCADE;
DROP TABLE IF EXISTS Ferry CASCADE;
DROP TABLE IF EXISTS Seaport CASCADE;
DROP TABLE IF EXISTS Route CASCADE;
DROP TABLE IF EXISTS Cabin CASCADE;
DROP TABLE IF EXISTS Cruise CASCADE;
DROP TABLE IF EXISTS Booking CASCADE;
DROP TABLE IF EXISTS Cabin_type CASCADE;
DROP TABLE IF EXISTS Vehicle_type CASCADE;

CREATE TABLE Account
(
    id        bigint             NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 ),
    version   bigint             NOT NULL,
    login     varchar(30)        NOT NULL,
    password  char(128)          NOT NULL,
    active    bool DEFAULT true  NOT NULL,
    confirmed bool DEFAULT false NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT login_unique UNIQUE (login)
);

ALTER TABLE Account
    OWNER TO ssbd02admin;

GRANT SELECT, INSERT, UPDATE ON TABLE Account TO ssbd02mok;

CREATE TABLE Personal_data
(
    id                       bigint                              NOT NULL,
    first_name               varchar(30)                         NOT NULL,
    last_name                varchar(50)                         NOT NULL,
    email                    varchar(70)                         NOT NULL,
    language                 varchar(5),
    time_zone                varchar(10),
    modification_date        timestamp,
    modified_by              bigint,
    creation_date            timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_known_good_login    timestamp,
    last_known_good_login_ip varchar(15),
    last_known_bad_login     timestamp,
    last_known_bad_login_ip  varchar(15),
    PRIMARY KEY (id),
    CONSTRAINT fk_account_id_modified_by FOREIGN KEY (modified_by) REFERENCES Account (id),
    CONSTRAINT fk_account_id_id FOREIGN KEY (id) REFERENCES Account (id),
    CONSTRAINT email_unique UNIQUE (email)
);

ALTER TABLE Personal_data
    OWNER TO ssbd02admin;

GRANT SELECT, INSERT, UPDATE ON TABLE Personal_data TO ssbd02mok;

CREATE TABLE Access_level
(
    id                bigint                              NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 ),
    version           bigint                              NOT NULL,
    level             varchar(16)                         NOT NULL,
    account_id        bigint                              NOT NULL,
    active            boolean   DEFAULT true              NOT NULL,
    modification_date timestamp,
    modified_by       bigint,
    creation_date     timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_account_id_modified_by FOREIGN KEY (modified_by) REFERENCES Account (id),
    CONSTRAINT fk_account_id FOREIGN KEY (account_id) REFERENCES Account (id),
    CONSTRAINT account_id_level_unique UNIQUE (account_id, level)
);

ALTER TABLE Access_level
    OWNER TO ssbd02admin;

GRANT SELECT, INSERT, UPDATE ON TABLE Access_level TO ssbd02mok;

CREATE TABLE Client_data
(
    id           bigint      NOT NULL,
    phone_number varchar(11),
    PRIMARY KEY (id),
    CONSTRAINT fk_access_level_id_id FOREIGN KEY (id) REFERENCES Access_level (id),
    CONSTRAINT phone_number_unique UNIQUE (phone_number)
);

ALTER TABLE Client_data
    OWNER TO ssbd02admin;

GRANT SELECT, INSERT, UPDATE ON TABLE Client_data TO ssbd02mok;

CREATE VIEW Auth_view AS
SELECT Account.login, Account.password, Access_level.level
FROM (Account JOIN Access_level ON
    ((Account.id = Access_level.Account_id)))
WHERE (((Account.confirmed = true) AND (Account.active = true))
    AND (Access_level.active = true));

ALTER TABLE public.Auth_view
    OWNER TO ssbd02admin;

GRANT SELECT ON TABLE Auth_view TO ssbd02auth;

CREATE TABLE Ferry
(
    id                bigint                              NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 ),
    version           bigint                              NOT NULL,
    name              varchar(30)                         NOT NULL,
    vehicle_capacity  int                                 NOT NULL,
    on_deck_capacity  int                                 NOT NULL,
    modification_date timestamp,
    modified_by       bigint,
    creation_date     timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by        bigint,
    PRIMARY KEY (id),
    CONSTRAINT vehicle_capacity_not_negative CHECK (vehicle_capacity >= 0),
    CONSTRAINT on_deck_capacity_greater_than_zero CHECK (on_deck_capacity > 0),
    CONSTRAINT ferry_name_unique UNIQUE (name),
    CONSTRAINT fk_account_id_modified_by FOREIGN KEY (modified_by) REFERENCES Account (id),
    CONSTRAINT fk_account_id_created_by FOREIGN KEY (created_by) REFERENCES Account (id)
);

ALTER TABLE Ferry
    OWNER TO ssbd02admin;

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE Ferry TO ssbd02mop;

CREATE TABLE Seaport
(
    id                bigint                              NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 ),
    version           bigint                              NOT NULL,
    city              varchar(30)                         NOT NULL,
    code              varchar(3)                          NOT NULL,
    modification_date timestamp,
    modified_by       bigint,
    creation_date     timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by        bigint,
    PRIMARY KEY (id),
    CONSTRAINT fk_account_id_modified_by FOREIGN KEY (modified_by) REFERENCES Account (id),
    CONSTRAINT fk_account_id_created_by FOREIGN KEY (created_by) REFERENCES Account (id),
    CONSTRAINT city_unique UNIQUE (city),
    CONSTRAINT seaport_code_unique UNIQUE (code)
);

ALTER TABLE Seaport
    OWNER TO ssbd02admin;

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE Seaport TO ssbd02mop;

CREATE TABLE Route
(
    id            bigint                              NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 ),
    version       bigint                              NOT NULL,
    start         bigint                              NOT NULL,
    destination   bigint                              NOT NULL,
    code          varchar(6)                          NOT NULL,
    creation_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by    bigint,
    PRIMARY KEY (id),
    CONSTRAINT fk_seaport_id_start FOREIGN KEY (start) REFERENCES Seaport (id),
    CONSTRAINT fk_seaport_id_destination FOREIGN KEY (destination) REFERENCES Seaport (id),
    CONSTRAINT fk_account_id_created_by FOREIGN KEY (created_by) REFERENCES Account (id),
    CONSTRAINT start_destination_unique UNIQUE (start, destination),
    CONSTRAINT route_code_unique UNIQUE (code)
);

ALTER TABLE Route
    OWNER TO ssbd02admin;

GRANT SELECT, INSERT, DELETE ON TABLE Route TO ssbd02mop;

CREATE TABLE Cabin_type
(
    id              bigint      NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 ),
    cabin_type_name varchar(30) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE Cabin_type
    OWNER TO ssbd02admin;

CREATE TABLE Cabin
(
    id                bigint                              NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 ),
    version           bigint                              NOT NULL,
    ferry             bigint                              NOT NULL,
    capacity          int                                 NOT NULL,
    cabin_type        bigint                              NOT NULL,
    number            varchar(4)                          NOT NULL,
    modification_date timestamp,
    modified_by       bigint,
    creation_date     timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by        bigint,
    PRIMARY KEY (id),
    CONSTRAINT capacity_greater_than_zero CHECK (capacity > 0),
    CONSTRAINT fk_ferry_id FOREIGN KEY (ferry) REFERENCES Ferry (id),
    CONSTRAINT fk_cabin_type_id FOREIGN KEY (cabin_type) REFERENCES Cabin_type (id),
    CONSTRAINT fk_account_id_modified_by FOREIGN KEY (modified_by) REFERENCES Account (id),
    CONSTRAINT fk_account_id_created_by FOREIGN KEY (created_by) REFERENCES Account (id),
    CONSTRAINT cabin_number_unique UNIQUE (number)
);

ALTER TABLE Cabin
    OWNER TO ssbd02admin;

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE Cabin TO ssbd02mop;

CREATE TABLE Cruise
(
    id                bigint                              NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 ),
    version           bigint                              NOT NULL,
    start_date        timestamp                           NOT NULL,
    end_date          timestamp                           NOT NULL,
    route             bigint                              NOT NULL,
    ferry             bigint                              NOT NULL,
    number            varchar(12)                         NOT NULL,
    modification_date timestamp,
    modified_by       bigint,
    creation_date     timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by        bigint,
    PRIMARY KEY (id),
    CONSTRAINT end_date_after_start_date CHECK (end_date > start_date),
    CONSTRAINT fk_route_id FOREIGN KEY (route) REFERENCES Route (id),
    CONSTRAINT fk_ferry_id FOREIGN KEY (ferry) REFERENCES Ferry (id),
    CONSTRAINT fk_account_id_modified_by FOREIGN KEY (modified_by) REFERENCES Account (id),
    CONSTRAINT fk_account_id_created_by FOREIGN KEY (created_by) REFERENCES Account (id),
    CONSTRAINT cruise_number_unique UNIQUE (number)
);

ALTER TABLE Cruise
    OWNER TO ssbd02admin;

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE Cruise TO ssbd02mop;

CREATE TABLE Vehicle_type
(
    id                bigint      NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 ),
    vehicle_type_name varchar(30) NOT NULL,
    required_space    float       NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT required_space_not_negative CHECK (required_space >= 0)
);

ALTER TABLE Vehicle_type
    OWNER TO ssbd02admin;

CREATE TABLE Booking
(
    id               bigint                              NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 ),
    version          bigint                              NOT NULL,
    cruise           bigint                              NOT NULL,
    account          bigint                              NOT NULL,
    number_of_people int                                 NOT NULL,
    cabin            bigint,
    vehicle_type     bigint                              NOT NULL,
    price            float                               NOT NULL,
    number           varchar(10)                         NOT NULL,
    creation_date    timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT number_of_people_greater_than_zero CHECK (number_of_people > 0),
    CONSTRAINT price_greater_than_zero CHECK (price > 0),
    CONSTRAINT fk_cruise_id FOREIGN KEY (cruise) REFERENCES Cruise (id),
    CONSTRAINT fk_account_id FOREIGN KEY (account) REFERENCES Account (id),
    CONSTRAINT fk_cabin_id FOREIGN KEY (cabin) REFERENCES Cabin (id),
    CONSTRAINT fk_vehicle_type_id FOREIGN KEY (vehicle_type) REFERENCES Vehicle_type (id),
    CONSTRAINT booking_number_unique UNIQUE (number)
);

ALTER TABLE Booking
    OWNER TO ssbd02admin;

GRANT SELECT, INSERT, DELETE ON TABLE Booking TO ssbd02mop;
