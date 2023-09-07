CREATE TABLE EXAMPLE (
                         ID SERIAL PRIMARY KEY,
                         FIRST_NAME VARCHAR(250) NOT NULL,
                         LAST_NAME VARCHAR(250) NOT NULL,
                         CAREER VARCHAR(250) DEFAULT NULL
);

CREATE TABLE CATEGORIES (
                            ID SERIAL PRIMARY KEY,
                            UID UUID NOT NULL,
                            PARENT_ID BIGINT,
                            NAME VARCHAR(100),
                            DESCRIPTION VARCHAR(1000),
                            ICON VARCHAR(45),
                            IMAGE_PATH VARCHAR(100),
                            CREATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            UPDATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE PRODUCTS (
                          ID SERIAL PRIMARY KEY,
                          UID UUID NOT NULL,
                          NAME VARCHAR(45),
                          SKU VARCHAR(45),
                          REGULAR_PRICE DOUBLE PRECISION,
                          DISCOUNT_PRICE DOUBLE PRECISION,
                          DISCOUNT_PRICE_END_DATE TIMESTAMP,
                          LOWEST_PRICE DOUBLE PRECISION,
                          DESCRIPTION VARCHAR(4000),
                          SHORT_DESCRIPTION VARCHAR(500),
                          NOTE VARCHAR(100),
                          STATUS VARCHAR(50),
                          PRODUCTSCOL VARCHAR(45),
                          QUANTITY INT,
                          CREATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          UPDATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
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
                              ID SERIAL PRIMARY KEY,
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
                          ID BIGINT PRIMARY KEY,
                          FIRST_NAME VARCHAR(250) NOT NULL,
                          LAST_NAME VARCHAR(250) NOT NULL,
                          USER_NAME VARCHAR(100) NOT NULL UNIQUE,
                          ROLE VARCHAR(50) NOT NULL,
                          PASSWORD VARCHAR(150),
                          CREATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          UPDATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          IS_ENABLED BOOLEAN DEFAULT TRUE,
                          UUID UUID DEFAULT gen_random_uuid()
);

CREATE TABLE SESSIONS (
                          ID SERIAL PRIMARY KEY,
                          UID UUID,
                          EXPIRATION_TIME TIMESTAMP,
                          IS_EXPIRED BOOLEAN
);

CREATE TABLE CARTS (
                       ID SERIAL PRIMARY KEY,
                       UUID UUID,
                       USER_ID BIGINT,
                       TOTAL_PRICE DOUBLE PRECISION,
                       TOTAL_QUANTITY INT,
                       CREATED_AT TIMESTAMP,
                       UPDATED_AT TIMESTAMP,
                       SESSION_ID BIGINT,
                       FOREIGN KEY (SESSION_ID) REFERENCES SESSIONS(ID)
);

CREATE TABLE CART_ITEMS (
                            ID SERIAL PRIMARY KEY,
                            CART_ID BIGINT,
                            PRODUCT_ID BIGINT,
                            QUANTITY INT,
                            FOREIGN KEY (CART_ID) REFERENCES CARTS (ID),
                            FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCTS (ID)
);