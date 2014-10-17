CREATE TABLE USER(
    ID IDENTITY,
    NAME VARCHAR(80) NOT NULL,
    PASSWORD VARCHAR(32) NOT NULL
);

ALTER TABLE USER ADD CONSTRAINT USER_UNQ_NAME
UNIQUE(NAME);

CREATE TABLE APPLICATION(
    ID IDENTITY,
    USER BIGINT NOT NULL,
    AMOUNT DECIMAL(20,2) NOT NULL,
    INTEREST DECIMAL(20,10) NOT NULL,
    DUE_DATE TIMESTAMP NOT NULL
);

ALTER TABLE APPLICATION ADD CONSTRAINT APPLICATION_FK_USER
FOREIGN KEY(USER) REFERENCES USER(ID);

CREATE TABLE EVENT(
    ID IDENTITY,
    USER BIGINT NOT NULL,
    APPLICATION BIGINT NULL,
    APPLICANT_IP VARCHAR(39) NOT NULL,
    STATUS VARCHAR(16) NOT NULL,
    EVENT_DATE TIMESTAMP NOT NULL
);

ALTER TABLE EVENT ADD CONSTRAINT EVENT_FK_USER
FOREIGN KEY(USER) REFERENCES USER(ID);

ALTER TABLE EVENT ADD CONSTRAINT EVENT_FK_APPLICATION
FOREIGN KEY(APPLICATION) REFERENCES APPLICATION(ID);

ALTER TABLE EVENT ADD CONSTRAINT EVENT_CK_STATUS
CHECK STATUS IN('APPLICATION', 'DENIED', 'EXTENSION');
