INSERT INTO EXAMPLE (FIRST_NAME, LAST_NAME, CAREER)
VALUES ('Bartosz', 'Stpiczynski', 'frontend dev'),
       ('Mateusz', 'Ryniewicz', 'frontend dev'),
       ('Marcin', 'Noga', 'backend dev'),
       ('Łukasz', 'Różyło', 'frontend dev'),
       ('Kamil', 'Chrzanowski', 'frontend dev'),
       ('Maciej', 'Marciniak', 'fullstack dev');

INSERT INTO APP_USER (FIRST_NAME, LAST_NAME, USER_NAME, PASSWORD, ROLE) VALUES
('Jan', 'Kowalski', 'user123@example.com',   '$2a$10$e3DPMZhiMGdS4sgXmS4N/uG.9Ar.NUHKX3iktP3oFu//zqaa4Zn8.', 'ROLE_USER'),
('John', 'Doe', 'john.doe@emailprovider.com',  '$2a$10$e3DPMZhiMGdS4sgXmS4N/uG.9Ar.NUHKX3iktP3oFu//zqaa4Zn8.', 'ROLE_USER'),
('Greg', 'Bizonet', 'myemail@email.com',   '$2a$10$e3DPMZhiMGdS4sgXmS4N/uG.9Ar.NUHKX3iktP3oFu//zqaa4Zn8.', 'ROLE_USER'),
('Dennis', 'Chumbers', 'abc_1234@email.net',   '$2a$10$e3DPMZhiMGdS4sgXmS4N/uG.9Ar.NUHKX3iktP3oFu//zqaa4Zn8.', 'ROLE_USER'),
('Tom', 'Random', 'random.email@example.org',   '$2a$10$e3DPMZhiMGdS4sgXmS4N/uG.9Ar.NUHKX3iktP3oFu//zqaa4Zn8.', 'ROLE_USER'),
('Vinnie', 'Colaiuta', 'yourmail@mailprovider.com',   '$2a$10$e3DPMZhiMGdS4sgXmS4N/uG.9Ar.NUHKX3iktP3oFu//zqaa4Zn8.', 'ROLE_USER'),
('Steve', 'Gadd', 'test_email@example.com' ,   '$2a$10$e3DPMZhiMGdS4sgXmS4N/uG.9Ar.NUHKX3iktP3oFu//zqaa4Zn8.', 'ROLE_USER'),
('Dave', 'Weckl', 'user456@emailprovider.net' ,   '$2a$10$e3DPMZhiMGdS4sgXmS4N/uG.9Ar.NUHKX3iktP3oFu//zqaa4Zn8.', 'ROLE_USER'),
('Admin', 'Administratorski', 'demo.email@example.org' ,   '$2a$10$e3DPMZhiMGdS4sgXmS4N/uG.9Ar.NUHKX3iktP3oFu//zqaa4Zn8.', 'ROLE_ADMIN'),
('Zenon', 'Nadszyszkownik', 'hello123@email.com' ,   '$2a$10$e3DPMZhiMGdS4sgXmS4N/uG.9Ar.NUHKX3iktP3oFu//zqaa4Zn8.', 'ROLE_ADMIN')
;