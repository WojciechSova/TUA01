DROP TABLE IF EXISTS Account CASCADE;
DROP TABLE IF EXISTS Access_level CASCADE;
DROP TABLE IF EXISTS Personal_data CASCADE;
DROP TABLE IF EXISTS Client_data CASCADE;


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

ALTER TABLE Account OWNER TO ssbd02admin;

GRANT SELECT, INSERT, UPDATE ON TABLE Account TO ssbd02mok;
GRANT SELECT ON TABLE Account TO ssbd02mok;

CREATE TABLE Personal_data
(
    id                       bigint                              NOT NULL,
    version                  bigint                              NOT NULL,
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
    CONSTRAINT fk_account_id_modified_by FOREIGN KEY (modified_by) REFERENCES account(id),
    CONSTRAINT fk_account_id_id FOREIGN KEY (id) REFERENCES account(id),
    CONSTRAINT email_unique UNIQUE (email)
);

ALTER TABLE Personal_data OWNER TO ssbd02admin;

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
    CONSTRAINT fk_account_id_modified_by FOREIGN KEY (modified_by) REFERENCES account(id),
    CONSTRAINT fk_account_id FOREIGN KEY (account_id) REFERENCES account(id),
    CONSTRAINT account_id_level_unique UNIQUE (account_id, level)
);

ALTER TABLE Access_level OWNER TO ssbd02admin;

GRANT SELECT, INSERT, UPDATE ON TABLE Access_level TO ssbd02mok;

CREATE TABLE Client_data
(
    id           bigint      NOT NULL,
    version      bigint      NOT NULL,
    phone_number varchar(11) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_access_level_id_id FOREIGN KEY (id) REFERENCES access_level(id),
    CONSTRAINT phone_number_unique UNIQUE (phone_number)
);

ALTER TABLE Client_data OWNER TO ssbd02admin;

GRANT SELECT, INSERT, UPDATE ON TABLE Client_data TO ssbd02mok;
