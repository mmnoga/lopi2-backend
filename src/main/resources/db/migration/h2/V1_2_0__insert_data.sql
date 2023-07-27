INSERT INTO EXAMPLE (FIRST_NAME, LAST_NAME, CAREER)
VALUES ('Bartosz', 'Stpiczynski', 'frontend dev'),
       ('Mateusz', 'Ryniewicz', 'frontend dev'),
       ('Marcin', 'Noga', 'backend dev'),
       ('Łukasz', 'Różyło', 'frontend dev'),
       ('Kamil', 'Chrzanowski', 'frontend dev'),
       ('Maciej', 'Marciniak', 'fullstack dev');

INSERT INTO CATEGORIES (ID, UID, PARENT_ID, NAME, DESCRIPTION, ICON, IMAGE_PATH, CREATED_AT, UPDATED_AT)
VALUES (1, '06e1899f-97c8-4d4e-aae3-712d31c45dd0', null, 'elektronika', 'opis kategorii elektronika', 'icon',
        'https://pixabay.com/photos/stock-iphone-business-mobile-phone-624712/', '2023-07-26 08:10:01.163454',
        '2023-07-26 08:10:01.163454'),
       (2, '68712e62-6fee-4bc0-9cd3-a94e63e3f494', 1, 'komputery', 'opis kategorii komputery', 'icon',
        'https://pixabay.com/photos/office-business-accountant-620822/', '2023-07-26 08:10:01.163454',
        '2023-07-26 08:10:01.163454'),
       (3, 'b663638d-ae89-4f5c-a6fe-f27cf41ae4e3', 2, 'laptopy', 'opis kategorii laptopy', 'icon',
        'https://pixabay.com/photos/macbook-laptop-google-display-459196/', '2023-07-26 08:10:01.163454',
        '2023-07-26 08:10:01.163454'),
       (4, 'd69b60ff-6d30-46a0-bc61-30ddab2d094c', 2, 'stacjonarne', 'opis kategorii stacjonarne', 'icon',
        'https://pixabay.com/photos/macbook-laptop-google-display-459196/', '2023-07-26 08:10:01.163454',
        '2023-07-26 08:10:01.163454');


INSERT INTO ROLE (ROLE_NAME) VALUES
('ROLE_USER'), ('ROLE_ADMIN');

INSERT INTO APP_USER (FIRST_NAME, LAST_NAME, EMAIL, CAREER, USER_PASS) VALUES
('Jan', 'Kowalski', 'user123@example.com',  'unknown', '$2a$10$e3DPMZhiMGdS4sgXmS4N/uG.9Ar.NUHKX3iktP3oFu//zqaa4Zn8.'), -- user
('John', 'Doe', 'john.doe@emailprovider.com',  'unknown', '$2a$10$e3DPMZhiMGdS4sgXmS4N/uG.9Ar.NUHKX3iktP3oFu//zqaa4Zn8.'), -- user
('Greg', 'Bizonet', 'myemail@email.com',  'unknown', '$2a$10$e3DPMZhiMGdS4sgXmS4N/uG.9Ar.NUHKX3iktP3oFu//zqaa4Zn8.'), -- user
('Dennis', 'Chumbers', 'abc_1234@email.net',  'unknown', '$2a$10$e3DPMZhiMGdS4sgXmS4N/uG.9Ar.NUHKX3iktP3oFu//zqaa4Zn8.'), -- user
('Tom', 'Rendom', 'random.email@example.org',  'unknown', '$2a$10$e3DPMZhiMGdS4sgXmS4N/uG.9Ar.NUHKX3iktP3oFu//zqaa4Zn8.'), -- user
('Vinnie', 'Colaiuta', 'yourmail@mailprovider.com',  'unknown', '$2a$10$e3DPMZhiMGdS4sgXmS4N/uG.9Ar.NUHKX3iktP3oFu//zqaa4Zn8.'), -- user
('Steve', 'Gadd', 'test_email@example.com' ,  'unknown', '$2a$10$e3DPMZhiMGdS4sgXmS4N/uG.9Ar.NUHKX3iktP3oFu//zqaa4Zn8.'), -- user
('Dave', 'Weckl', 'user456@emailprovider.net' ,  'unknown', '$2a$10$e3DPMZhiMGdS4sgXmS4N/uG.9Ar.NUHKX3iktP3oFu//zqaa4Zn8.'), -- user
('Admin', 'Administratorski', 'demo.email@example.org' ,  'unknown', '$2a$10$e3DPMZhiMGdS4sgXmS4N/uG.9Ar.NUHKX3iktP3oFu//zqaa4Zn8.'), -- admin
('Zenon', 'Nadszyszkownik', 'hello123@email.com' ,  'unknown', '$2a$10$e3DPMZhiMGdS4sgXmS4N/uG.9Ar.NUHKX3iktP3oFu//zqaa4Zn8.') -- admin
;

INSERT INTO APP_USER_ROLE (ID_APP_USER, ID_ROLE) VALUES
(1,1),(2,1),(3,1),(4,1),(5,1),(6,1),(7,1),(8,1),(9,2),(10,2);








