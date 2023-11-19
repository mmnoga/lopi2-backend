CREATE TABLE REVIEWS (
                        ID              BIGINT AUTO_INCREMENT PRIMARY KEY,
                        UID             UUID NOT NULL,
                        PRODUCT_UID     UUID NOT NULL,
                        RATING          INT NOT NULL,
                        REVIEW_TEXT     VARCHAR(500),
                        USER_ID         BIGINT,
                        IS_VISIBLE      BOOLEAN
);