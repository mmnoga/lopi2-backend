CREATE TABLE REVIEWS (
                         ID              BIGSERIAL PRIMARY KEY,
                         UID             UUID NOT NULL,
                         PRODUCT_UID     UUID NOT NULL,
                         RATING          INTEGER NOT NULL,
                         REVIEW_TEXT     VARCHAR(500),
                         USER_ID         BIGINT,
                         IS_VISIBLE      BOOLEAN
);