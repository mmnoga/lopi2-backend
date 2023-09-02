CREATE TABLE EXAMPLE (
                              ID BIGINT AUTO_INCREMENT  PRIMARY KEY,
                              FIRST_NAME VARCHAR(250) NOT NULL,
                              LAST_NAME VARCHAR(250) NOT NULL,
                              CAREER VARCHAR(250) DEFAULT NULL
);

CREATE TABLE CATEGORIES (
                          ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                          UID UUID NOT NULL,
                          PARENT_ID LONG,
                          NAME VARCHAR(100),
                          DESCRIPTION VARCHAR(1000),
                          ICON VARCHAR(45),
                          IMAGE_PATH VARCHAR(100),
                          CREATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          UPDATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE PRODUCTS (
                         ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                         UID UUID NOT NULL,
                         NAME VARCHAR(45),
                         SKU VARCHAR(45),
                         REGULAR_PRICE DOUBLE,
                         DISCOUNT_PRICE DOUBLE,
                         DISCOUNT_PRICE_END_DATE DATETIME,
                         LOWEST_PRICE DOUBLE,
                         DESCRIPTION VARCHAR(4000),
                         SHORT_DESCRIPTION VARCHAR(500),
                         NOTE VARCHAR(100),
                         STATUS VARCHAR(50),
                         PRODUCTSCOL VARCHAR(45),
                         QUANTITY INT,
                         CREATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         UPDATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         ARCHIVED_AT TIMESTAMP
);

CREATE TABLE PRODUCTS_CATEGORIES (
                                     PRODUCTS_ID BIGINT NOT NULL,
                                     CATEGORIES_ID BIGINT NOT NULL,
                                     PRIMARY KEY (PRODUCTS_ID, CATEGORIES_ID),
                                     FOREIGN KEY (PRODUCTS_ID) REFERENCES PRODUCTS(ID),
                                     FOREIGN KEY (CATEGORIES_ID) REFERENCES CATEGORIES(ID)
);

CREATE TABLE IMAGE_ASSETS (
                                ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                                ASSET_URL VARCHAR(255)
);

CREATE TABLE PRODUCTS_IMAGE_ASSETS (
                                       PRODUCT_ID BIGINT NOT NULL,
                                       IMAGE_ASSETS_ID BIGINT NOT NULL,
                                       PRIMARY KEY (PRODUCT_ID, IMAGE_ASSETS_ID),
                                       FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCTS(ID),
                                       FOREIGN KEY (IMAGE_ASSETS_ID) REFERENCES IMAGE_ASSETS(ID)
);

CREATE TABLE APP_USER (
                                ID BIGINT AUTO_INCREMENT  PRIMARY KEY,
                                FIRST_NAME VARCHAR(250) NOT NULL,
                                LAST_NAME VARCHAR(250) NOT NULL,
                                USER_NAME VARCHAR(100) NOT NULL UNIQUE,
                                ROLE VARCHAR(50) NOT NULL,
                                PASSWORD VARCHAR(150),
                                CREATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                UPDATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                IS_ENABLED BOOLEAN DEFAULT TRUE,
                                UUID UUID DEFAULT RANDOM_UUID()
);


CREATE TABLE ADDRESS (
                         ID BIGINT AUTO_INCREMENT PRIMARY KEY,
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
                         ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                         ORDER_ID BIGINT,
                         PRODUCT_ID BIGINT,
                         QUANTITY INT,
                         PRICE_PER_UNIT DOUBLE,
                         SUBTOTAL DOUBLE,
                         CREATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         UPDATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP


);


CREATE TABLE ORDERS (
                         ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                         UID UUID NOT NULL,
                         USER_ID BIGINT,
                         CUSTOMER_ID BIGINT,
                         ORDER_DATE TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         STATUS VARCHAR(50),
                         TOTAL_PRICE DOUBLE,
                         DELIVERY_METHOD_ID BIGINT,
                         CART_ID BIGINT,
                         DELIVERY_COST DOUBLE,
                         SHIPPING_ADDRESS_ID BIGINT,
                         BILLING_ADDRESS_ID BIGINT,
                         PAYMENT_METHOD_ID BIGINT,
                         TERMS_ACCEPTED BOOLEAN,
                         CART_UUID UUID,
                         CREATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         UPDATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP


);

CREATE TABLE CARTS (
                       ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                       UUID UUID,
                       USER_ID BIGINT,
                       TOTAL_PRICE DOUBLE PRECISION,
                       TOTAL_QUANTITY INT,
                       CREATED_AT TIMESTAMP,
                       UPDATED_AT TIMESTAMP,
                       SESSION_ID BIGINT
);

CREATE TABLE SESSIONS (
                          ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                          UID UUID,
                          EXPIRATION_TIME TIMESTAMP,
                          IS_EXPIRED BOOLEAN
);
CREATE TABLE PAYMENT_METHODS (
                                 ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 NAME VARCHAR(255) UNIQUE,
                                 DESCRIPTION VARCHAR(255)
);

CREATE TABLE DELIVERY_METHODS (
                                 ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 NAME VARCHAR(255) UNIQUE,
                                 DESCRIPTION VARCHAR(255),
                                 COST DOUBLE PRECISION
);
ALTER TABLE CARTS
    ADD FOREIGN KEY (SESSION_ID) REFERENCES SESSIONS(ID);

CREATE TABLE CART_ITEMS (
                            ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                            CART_ID BIGINT,
                            PRODUCT_ID BIGINT,
                            QUANTITY INT,
                            FOREIGN KEY (CART_ID) REFERENCES CARTS (ID),
                            FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCTS (ID)
);

CREATE TABLE CUSTOMERS (
                           ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                           CUSTOMER_TYPE VARCHAR(50),
                           NIP VARCHAR(20),
                           COMPANY_NAME VARCHAR(255),
                           SALUTATION VARCHAR(50),
                           FIRST_NAME VARCHAR(255),
                           LAST_NAME VARCHAR(255),
                           EMAIL VARCHAR(255)
);
