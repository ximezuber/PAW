CREATE TABLE IF NOT EXISTS locations (
    name VARCHAR(30) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS specialties (
    name VARCHAR(30) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS users (
    firstName VARCHAR(20),
    lastName varchar(20),
    password VARCHAR(60),
    email VARCHAR(25) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS admins (
    email VARCHAR(25) PRIMARY KEY REFERENCES users(email) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS doctors (
    license VARCHAR(20) PRIMARY KEY,
    specialty VARCHAR(50) REFERENCES specialties(name) ON UPDATE CASCADE ON DELETE CASCADE,
    email VARCHAR(25) REFERENCES users(email) ON UPDATE CASCADE ON DELETE CASCADE,
    phoneNumber VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS clinics (
    id IDENTITY PRIMARY KEY,
    name VARCHAR(20),
    location VARCHAR(30) REFERENCES locations(name) ON UPDATE CASCADE ON DELETE CASCADE,
    address VARCHAR(45) NOT NULL
);

CREATE TABLE IF NOT EXISTS prepaids (
    name VARCHAR(30) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS clinicPrepaids(
    clinicid INTEGER REFERENCES clinics(id) ON UPDATE CASCADE ON DELETE CASCADE,
    prepaid VARCHAR(30) REFERENCES prepaids(name) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS favorites(
    patientEmail VARCHAR(25) REFERENCES users(email) ON UPDATE CASCADE ON DELETE CASCADE,
    doctorLicense VARCHAR(20) REFERENCES doctors(license) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS doctorclinics (
    doctorLicense VARCHAR(20) REFERENCES doctors(license) ON UPDATE CASCADE ON DELETE CASCADE,
    clinicid INTEGER REFERENCES clinics(id) ON UPDATE CASCADE ON DELETE CASCADE,
    consultPrice INTEGER,
    PRIMARY KEY(doctorLicense, clinicid)
);

CREATE TABLE IF NOT EXISTS schedule (
    day INTEGER,
    hour INTEGER,
    doctor VARCHAR(20),
    clinic INTEGER,
    PRIMARY KEY (day, hour, doctor),
    FOREIGN KEY (doctor, clinic) REFERENCES doctorclinics(doctorLicense, clinicid) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS patients (
    email VARCHAR(25) PRIMARY KEY REFERENCES users(email) ON UPDATE CASCADE ON DELETE CASCADE,
    id varchar(8),
    prepaid VARCHAR(20) REFERENCES prepaids(name) ON UPDATE CASCADE ON DELETE SET NULL,
    prepaidNumber varchar(20)
);

CREATE TABLE IF NOT EXISTS appointments (
    doctor VARCHAR(20),
    clinic INTEGER,
    patient VARCHAR(25) REFERENCES users(email) ON UPDATE CASCADE ON DELETE CASCADE,
    date TIMESTAMP,
    PRIMARY KEY (doctor, patient, date),
    FOREIGN KEY (doctor, clinic) REFERENCES doctorclinics(doctorLicense, clinicid) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS images (
    id IDENTITY PRIMARY KEY,
    doctor VARCHAR(20) REFERENCES doctors(license) ON UPDATE CASCADE ON DELETE CASCADE,
    image binary
);

CREATE TABLE IF NOT EXISTS favorites (
    patient VARCHAR(25) REFERENCES patients(email) ON UPDATE CASCADE ON DELETE CASCADE,
    doctor VARCHAR(20) REFERENCES doctors(license) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (patient, doctor)
);

/* Populates DB with locations */

INSERT INTO locations(name)
    SELECT * FROM (VALUES ('location'))
    WHERE NOT EXISTS (SELECT * FROM locations WHERE name='location');

/* Populates DB with specialties */

INSERT INTO specialties(name)
    SELECT * FROM (VALUES ('specialty'))
    WHERE NOT EXISTS (SELECT * FROM specialties  WHERE name='specialty');

/* Populates DB with users */

INSERT INTO users(firstName, lastName, password, email)
    SELECT * FROM (VALUES ('admin', '', 'admin', 'admin@mail.com'))
    WHERE NOT EXISTS (SELECT * FROM users  WHERE email='admin@mail.com');
INSERT INTO users(firstName, lastName, password, email)
    SELECT * FROM (VALUES ('docFirstName', 'docLastName', 'password', 'doctor@mail.com'))
    WHERE NOT EXISTS (SELECT * FROM users  WHERE email='doctor@mail.com');
INSERT INTO users(firstName, lastName, password, email)
    SELECT * FROM (VALUES ('docFirstName2', 'docLastName2', 'password', 'doctor2@mail.com'))
    WHERE NOT EXISTS (SELECT * FROM users  WHERE email='doctor2@mail.com');
INSERT INTO users(firstName, lastName, password, email)
    SELECT * FROM (VALUES ('docFirstName3', 'docLastName3', 'password', 'doctor3@mail.com'))
    WHERE NOT EXISTS (SELECT * FROM users  WHERE email='doctor3@mail.com');
INSERT INTO users(firstName, lastName, password, email)
    SELECT * FROM (VALUES ('docFirstName4', 'docLastName4', 'password', 'doctor4@mail.com'))
    WHERE NOT EXISTS (SELECT * FROM users  WHERE email='doctor4@mail.com');
INSERT INTO users(firstName, lastName, password, email)
    SELECT * FROM (VALUES ('patFirstName', 'patLastName', 'password', 'patient@mail.com'))
    WHERE NOT EXISTS (SELECT * FROM users  WHERE email='patient@mail.com');
INSERT INTO users(firstName, lastName, password, email)
    SELECT * FROM (VALUES ('patFirstName2', 'patLastName2', 'password', 'patient2@mail.com'))
    WHERE NOT EXISTS (SELECT * FROM users  WHERE email='patient2@mail.com');

/* Populates DB with doctors */

INSERT INTO doctors(license, specialty, email, phoneNumber)
    SELECT * FROM (VALUES ('1', 'specialty', 'doctor@mail.com', '1234567890'))
    WHERE NOT EXISTS (SELECT * FROM doctors  WHERE license='1');
INSERT INTO doctors(license, specialty, email, phoneNumber)
    SELECT * FROM (VALUES ('2', 'specialty', 'doctor2@mail.com', '12567890'))
    WHERE NOT EXISTS (SELECT * FROM doctors  WHERE license='2');
INSERT INTO doctors(license, specialty, email, phoneNumber)
    SELECT * FROM (VALUES ('3', 'specialty', 'doctor3@mail.com', '1234'))
    WHERE NOT EXISTS (SELECT * FROM doctors  WHERE license='3');


/* Populates DB with prepaids */

INSERT INTO prepaids(name)
    SELECT * FROM (VALUES ('prepaid1'))
    WHERE NOT EXISTS (SELECT * FROM prepaids  WHERE name='prepaid1');
INSERT INTO prepaids(name)
    SELECT * FROM (VALUES ('prepaid2'))
    WHERE NOT EXISTS (SELECT * FROM prepaids  WHERE name='prepaid2');

/* Populates DB with clinics */

INSERT INTO clinics(id, name, location, address)
    SELECT * FROM (VALUES (1, 'clinic', 'location', 'address'))
    WHERE NOT EXISTS (SELECT * FROM clinics  WHERE id=1);
INSERT INTO clinics(id, name, location, address)
    SELECT * FROM (VALUES (2, 'clinic2', 'location', 'address2'))
    WHERE NOT EXISTS (SELECT * FROM clinics  WHERE id=2);


/* Populates DB with clinics */

INSERT INTO doctorclinics(doctorLicense, clinicid, consultPrice)
    SELECT * FROM (VALUES ('1', 1, 1))
    WHERE NOT EXISTS (SELECT * FROM doctorclinics WHERE doctorLicense=1 AND clinicid=1);
INSERT INTO doctorclinics(doctorLicense, clinicid, consultPrice)
    SELECT * FROM (VALUES ('2', 1, 1))
    WHERE NOT EXISTS (SELECT * FROM doctorclinics WHERE doctorLicense=2 AND clinicid=1);


/* Populates DB with clinicPrepaids */

INSERT INTO clinicPrepaids(clinicid, prepaid)
    SELECT * FROM (VALUES ('1', 'prepaid1'))
    WHERE NOT EXISTS (SELECT * FROM clinicPrepaids WHERE clinicid=1 AND prepaid='prepaid1');


/* Populates DB with schedule */

INSERT INTO schedule(day, hour, doctor, clinic)
    SELECT * FROM (VALUES (3, 8, '1', 1))
    WHERE NOT EXISTS (SELECT * FROM schedule WHERE day=3 AND hour=8 AND doctor='1');

/* Populates DB with patients */

INSERT INTO patients(email, id, prepaid, prepaidNumber)
    SELECT * FROM (VALUES ('patient@mail.com', '12345678', 'prepaid1', '111'))
    WHERE NOT EXISTS (SELECT * FROM patients WHERE email='patient@mail.com');
INSERT INTO patients(email, id, prepaid, prepaidNumber)
    SELECT * FROM (VALUES ('patient2@mail.com', '23456789', 'prepaid1', '123'))
    WHERE NOT EXISTS (SELECT * FROM patients WHERE email='patient2@mail.com');



/* Populates DB with appointments */

INSERT INTO appointments(doctor, clinic, patient, date)
    SELECT * FROM (VALUES ('1', 1, 'patient@mail.com', TIMESTAMP '2019-10-01 08:00:00'))
    WHERE NOT EXISTS (SELECT * FROM appointments WHERE doctor='1' AND patient='patient@mail.com' AND date=(TIMESTAMP '2019-10-01 08:00:00'));

