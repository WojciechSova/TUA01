DROP TABLE IF EXISTS Account CASCADE;
DROP TABLE IF EXISTS Access_level CASCADE;
DROP VIEW IF EXISTS Auth_view CASCADE;


CREATE TABLE Account (
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 ),
    version integer NOT NULL,
	login varchar(30) NOT NULL,
    name varchar(30) NOT NULL,
    surname varchar(50) NOT NULL,
    email varchar(100) NOT NULL,
    phone_number varchar(12) NOT NULL,
    password char(128) NOT NULL,
    active bool DEFAULT true NOT NULL,
	confirmed bool DEFAULT false NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT email_unique unique (email),
    CONSTRAINT login_unique unique (login)
);

ALTER TABLE public.account OWNER TO ssbd02admin;

GRANT SELECT, INSERT, UPDATE ON TABLE Account TO ssbd02mok;

CREATE TABLE Access_level (
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 ),
	version integer NOT NULL,
    level character varying(16) NOT NULL,
    account_id bigint NOT NULL,
    active boolean DEFAULT true NOT NULL,
	PRIMARY KEY (id),
	CONSTRAINT fk_account_id FOREIGN KEY (account_id) REFERENCES account(id),
	CONSTRAINT account_id_level_unique UNIQUE (account_id, level)
);

ALTER TABLE public.Access_level OWNER TO ssbd02admin;

GRANT SELECT, INSERT, UPDATE ON TABLE Access_level TO ssbd02mok;

CREATE VIEW Auth_view AS
    SELECT Account.login, Account.password, Access_level.level FROM (Account JOIN Access_level ON
        ((Account.id = Access_level.Account_id))) WHERE (((Account.confirmed = true) AND (Account.active = true))
                                                             AND (Access_level.active = true));

ALTER TABLE public.Auth_view OWNER TO ssbd02admin;

GRANT SELECT ON TABLE Auth_view TO ssbd02auth;
