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
    id SERIAL PRIMARY KEY,
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
    id SERIAL PRIMARY KEY,
    doctor VARCHAR(20) REFERENCES doctors(license) ON UPDATE CASCADE ON DELETE CASCADE,
    image bytea
);

CREATE TABLE IF NOT EXISTS favorites (
    patient VARCHAR(25) REFERENCES patients(email) ON UPDATE CASCADE ON DELETE CASCADE,
    doctor VARCHAR(20) REFERENCES doctors(license) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (patient, doctor)
);
