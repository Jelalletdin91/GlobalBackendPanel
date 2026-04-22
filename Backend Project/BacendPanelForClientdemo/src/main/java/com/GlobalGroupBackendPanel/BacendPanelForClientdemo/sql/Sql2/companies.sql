CREATE TABLE companies (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    subscription_plan VARCHAR(50),
    subscription_start DATE,
    subscription_end DATE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id)
);