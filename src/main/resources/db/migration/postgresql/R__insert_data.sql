DELETE FROM APP_USER WHERE USER_NAME = 'admin@example.org';

INSERT INTO APP_USER (ID, FIRST_NAME, LAST_NAME, USER_NAME, PASSWORD, ROLE) VALUES
       (1, 'Admin', 'Administratorski', 'admin@example.org' , '$2a$10$1YFQa/2ctYg62/P4aXGKeOmB2snioaP4vDtbou8OMy64dFEbu1N9u', 'ROLE_ADMIN');