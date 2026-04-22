USE kimlik_notification;

CREATE TABLE student_clients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    phone_number VARCHAR(30) NOT NULL,

    kimlik_number VARCHAR(20) NOT NULL UNIQUE,
    kimlik_start_date DATE NOT NULL,
    kimlik_end_date DATE NOT NULL,

    university VARCHAR(150) NOT NULL,
    department VARCHAR(150) NOT NULL,

    study_year_begin DATE NOT NULL,
    study_year_end DATE NOT NULL,

    notified_60_days BOOLEAN DEFAULT FALSE,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_student_kimlik_end_date
ON student_clients (kimlik_end_date);