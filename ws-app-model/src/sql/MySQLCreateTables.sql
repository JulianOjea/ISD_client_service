-- ----------------------------------------------------------------------------
-- Model
-------------------------------------------------------------------------------
DROP TABLE Registration;
DROP TABLE RunningRace;

CREATE TABLE RunningRace(
    id BIGINT NOT NULL AUTO_INCREMENT,
    city VARCHAR(255) NOT NULL,
    description VARCHAR(8000),
    raceDate TIMESTAMP NOT NULL,
    price FLOAT NOT NULL,
    maxParticipants INT NOT NULL,
    creationDate TIMESTAMP NOT NULL,
    totalRegistrations INT NOT NULL DEFAULT(0),
    CONSTRAINT RunningRacePK PRIMARY KEY (id),
    CONSTRAINT validRacePrice CHECK( price >= 0)
)ENGINE = InnoDB;

CREATE TABLE Registration(
    id BIGINT NOT NULL AUTO_INCREMENT,
    runningRaceId BIGINT NOT NULL,
    userMail VARCHAR(255) NOT NULL,
    creditCard CHAR(16) NOT NULL,
    dorsal INT,
    dorsalPickedUp BIT NOT NULL DEFAULT(0),
    registrationDate TIMESTAMP NOT NULL,
    CONSTRAINT RegistrationPK PRIMARY KEY(id),
    CONSTRAINT RegistrationRunningRaceFK FOREIGN KEY(runningRaceId)
        REFERENCES RunningRace(id) ON DELETE CASCADE
)ENGINE = InnoDB;
