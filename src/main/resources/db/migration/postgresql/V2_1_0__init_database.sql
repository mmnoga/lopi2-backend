

-------------------------------------------------------------

CREATE TABLE ADDRESS (
                         ID BIGSERIAL PRIMARY KEY,
                         STREET VARCHAR(250),
                         HOUSE_NUMBER VARCHAR(45),
                         APARTMENT_NUMBER VARCHAR(45),
                         POSTAL_CODE VARCHAR(45),
                         CITY VARCHAR(45),
                         COUNTRY VARCHAR(45),
                         PHONE_NUMBER VARCHAR(45),
                         CREATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         UPDATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

);

CREATE TABLE ORDER_ITEM (
                         ID BIGSERIAL PRIMARY KEY,
                         ORDER_ID BIGINT,
                         PRODUCT_ID BIGINT,
                         QUANTITY INTEGER,
                         PRICE_PER_UNIT DOUBLE PRECISION,
                         SUBTOTAL DOUBLE PRECISION,
                         CREATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         UPDATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP


);


CREATE TABLE ORDERS (
                         ID BIGSERIAL PRIMARY KEY,
                         UID UUID NOT NULL,
                         USER_ID BIGINT,
                         CUSTOMER_ID BIGINT,
                         ORDER_DATE TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         STATUS VARCHAR(50),
                         TOTAL_PRICE DOUBLE PRECISION,
                         DELIVERY_METHOD_ID BIGINT,
                         CART_ID BIGINT,
                         DELIVERY_COST DOUBLE PRECISION,
                         SHIPPING_ADDRESS_ID BIGINT,
                         BILLING_ADDRESS_ID BIGINT,
                         PAYMENT_METHOD_ID BIGINT,
                         TERMS_ACCEPTED BOOLEAN,
                         CART_UUID UUID,
                         CREATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         UPDATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP


);

CREATE TABLE CUSTOMERS (
                           ID BIGSERIAL PRIMARY KEY,
                           CUSTOMER_TYPE VARCHAR(50),
                           NIP VARCHAR(20),
                           COMPANY_NAME VARCHAR(255),
                           SALUTATION VARCHAR(50),
                           FIRST_NAME VARCHAR(255),
                           LAST_NAME VARCHAR(255),
                           EMAIL VARCHAR(255)
);
CREATE TABLE PAYMENT_METHODS (
                                 ID BIGSERIAL PRIMARY KEY,
                                 NAME VARCHAR(255) UNIQUE,
                                 DESCRIPTION VARCHAR(255)
);

CREATE TABLE DELIVERY_METHODS (
                                 ID BIGSERIAL PRIMARY KEY,
                                 NAME VARCHAR(255) UNIQUE,
                                 DESCRIPTION VARCHAR(255),
                                 COST DOUBLE PRECISION
);