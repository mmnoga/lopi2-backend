DELETE FROM APP_USER WHERE USER_NAME = 'admin@example.org';

INSERT INTO APP_USER (FIRST_NAME, LAST_NAME, USER_NAME, 'PHONE_NUMBER', PASSWORD, ROLE) VALUES
       ('Admin', 'Administratorski', 'admin@example.org' , '123 456 789', '$2a$10$1YFQa/2ctYg62/P4aXGKeOmB2snioaP4vDtbou8OMy64dFEbu1N9u', 'ROLE_ADMIN');