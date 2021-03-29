DROP TABLE IF EXISTS Account CASCADE;
DROP TABLE IF EXISTS Access_level CASCADE;
DROP VIEW IF EXISTS Auth_view CASCADE;


CREATE TABLE Account
(
    id                       bigint                              NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 ),
    version                  bigint                              NOT NULL,
    login                    varchar(30)                         NOT NULL,
    first_name               varchar(30)                         NOT NULL,
    last_name                varchar(50)                         NOT NULL,
    email                    varchar(70)                         NOT NULL,
    password                 char(128)                           NOT NULL,
    active                   bool      DEFAULT true              NOT NULL,
    confirmed                bool      DEFAULT false             NOT NULL,
    modification_date        timestamp,
    modified_by              bigint,
    creation_date            timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by               bigint,
    language                 varchar(5),
    last_known_good_login    timestamp,
    last_known_good_login_ip varchar(15),
    last_known_bad_login     timestamp,
    last_known_bad_login_ip  varchar(15),
    time_zone                varchar(10),
    PRIMARY KEY (id),
    CONSTRAINT fk_account_id_modified_by FOREIGN KEY (modified_by) REFERENCES account(id),
    CONSTRAINT fk_account_id_created_by FOREIGN KEY (created_by) REFERENCES account(id),
    CONSTRAINT email_unique UNIQUE (email),
    CONSTRAINT login_unique UNIQUE (login)
);

ALTER TABLE public.account OWNER TO ssbd02admin;

GRANT SELECT, INSERT, UPDATE ON TABLE Account TO ssbd02mok;

CREATE TABLE Access_level
(
    id         bigint                NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 ),
    version    bigint                NOT NULL,
    level      character varying(16) NOT NULL,
    account_id bigint                NOT NULL,
    active     boolean DEFAULT true  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_account_id FOREIGN KEY (account_id) REFERENCES account(id),
    CONSTRAINT account_id_level_unique UNIQUE (account_id, level)
);

ALTER TABLE public.Access_level OWNER TO ssbd02admin;

GRANT SELECT, INSERT, UPDATE ON TABLE Access_level TO ssbd02mok;

CREATE VIEW Auth_view AS
SELECT Account.login, Account.password, Access_level.level
FROM (Account JOIN Access_level ON
    ((Account.id = Access_level.Account_id)))
WHERE (((Account.confirmed = true) AND (Account.active = true))
    AND (Access_level.active = true));

ALTER TABLE public.Auth_view OWNER TO ssbd02admin;

GRANT SELECT ON TABLE Auth_view TO ssbd02auth;
