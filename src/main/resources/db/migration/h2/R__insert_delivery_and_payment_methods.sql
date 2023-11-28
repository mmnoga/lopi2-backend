DELETE FROM DELIVERY_METHODS;

DELETE FROM PAYMENT_METHODS;

INSERT INTO DELIVERY_METHODS (NAME, DESCRIPTION, COST) VALUES
       ('INPOST', 'InPost', 10.0),
       ('COURIER_SERVICE', 'Przesyłka kurierska', 20.0),
       ('IN_STORE_PICKUP', 'Odbiór osobisty w sklepie stacjonarnym', 0.0);

INSERT INTO PAYMENT_METHODS (NAME, DESCRIPTION) VALUES
       ('BLIK', 'Blik'),
       ('CREDIT_CARD', 'Karta kredytowa'),
       ('BANK_TRANSFER', 'Przelew bankowy'),
       ('ONLINE_TRANSFER', 'Szybki przelew online'),
       ('COD', 'Płatność przy odbiorze'),
       ('PAYU', 'Płatność PayU');