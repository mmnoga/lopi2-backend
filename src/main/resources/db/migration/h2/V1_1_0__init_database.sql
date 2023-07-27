CREATE TABLE EXAMPLE (
                              ID BIGINT AUTO_INCREMENT  PRIMARY KEY,
                              FIRST_NAME VARCHAR(250) NOT NULL,
                              LAST_NAME VARCHAR(250) NOT NULL,
                              CAREER VARCHAR(250) DEFAULT NULL
);

CREATE TABLE CATEGORIES (
                          ID INT AUTO_INCREMENT PRIMARY KEY,
                          UID UUID NOT NULL,
                          PARENT_ID INT,
                          NAME VARCHAR(100),
                          DESCRIPTION VARCHAR(1000),
                          ICON VARCHAR(45),
                          IMAGE_PATH VARCHAR(100),
                          CREATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          UPDATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE PRODUCTS (
                         ID INT AUTO_INCREMENT PRIMARY KEY,
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
                         PUBLISHED BOOLEAN,
                         PRODUCTSCOL VARCHAR(45),
                         QUANTITY INT,
                         CREATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         UPDATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE PRODUCTS_CATEGORIES (
                                     PRODUCTS_ID INT NOT NULL,
                                     CATEGORIES_ID INT NOT NULL,
                                     PRIMARY KEY (PRODUCTS_ID, CATEGORIES_ID),
                                     FOREIGN KEY (PRODUCTS_ID) REFERENCES PRODUCTS(ID),
                                     FOREIGN KEY (CATEGORIES_ID) REFERENCES CATEGORIES(ID)
);

CREATE TABLE APP_USER (
                                ID BIGINT AUTO_INCREMENT  PRIMARY KEY,
                                FIRST_NAME VARCHAR(250) NOT NULL,
                                LAST_NAME VARCHAR(250) NOT NULL,
                                EMAIL VARCHAR(100) NOT NULL UNIQUE,

                                USER_PASS VARCHAR(150),
                                CREATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                UPDATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                IS_ENABLED INTEGER DEFAULT 1,
                                UUID UUID DEFAULT RANDOM_UUID()
);

CREATE TABLE ROLE (
                                ID BIGINT AUTO_INCREMENT  PRIMARY KEY,
                                ROLE_NAME VARCHAR(50) NOT NULL,
                                UUID UUID DEFAULT RANDOM_UUID()
);
CREATE TABLE APP_USER_ROLE (

                                ID BIGINT AUTO_INCREMENT  PRIMARY KEY,
                                ID_APP_USER  BIGINT,
                                ID_ROLE BIGINT,
                                UUID UUID DEFAULT RANDOM_UUID(),
                                FOREIGN KEY (ID_APP_USER) REFERENCES APP_USER(ID),
                                FOREIGN KEY (ID_ROLE) REFERENCES ROLE(ID)
);

