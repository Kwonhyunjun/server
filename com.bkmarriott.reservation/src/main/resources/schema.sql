DROP TABLE IF EXISTS M_RESERVATION;
DROP TABLE IF EXISTS M_ROOM_TYPE_INVENTORY;

CREATE TABLE M_RESERVATION
(
    ID         BIGINT AUTO_INCREMENT
        PRIMARY KEY,
    USER_ID    BIGINT                                                        NOT NULL,
    HOTEL_ID   BIGINT                                                        NOT NULL,
    ROOM_ID    BIGINT                                                        NULL,
    START_DATE DATETIME(6)                                                   NOT NULL,
    END_DATE   DATETIME(6)                                                   NOT NULL,
    ROOM_TYPE  ENUM ('DELUXE', 'STANDARD', 'SUITE', 'TWIN')                  NOT NULL,
    STATUS     ENUM ('CANCELLED', 'PAID', 'PENDING', 'REFUNDED', 'REJECTED') NOT NULL,
    IS_DELETED      BIT                                          NOT NULL,
    CREATED_AT      DATETIME(6)                                  NOT NULL,
    CREATED_BY      BIGINT                                       NOT NULL,
    UPDATED_AT      DATETIME(6)                                  NOT NULL,
    UPDATED_BY      BIGINT                                       NOT NULL,
    DELETED_AT      DATETIME(6)                                  NULL,
    DELETED_BY      BIGINT                                       NULL
);

CREATE TABLE M_ROOM_TYPE_INVENTORY
(
    DATE            DATETIME(6)                                  NOT NULL,
    HOTEL_ID        BIGINT                                       NOT NULL,
    ROOM_TYPE       ENUM ('DELUXE', 'STANDARD', 'SUITE', 'TWIN') NOT NULL,
    PRIMARY KEY (DATE, HOTEL_ID, ROOM_TYPE),
    TOTAL_INVENTORY INT                                          NOT NULL,
    TOTAL_RESERVED  INT                                          NOT NULL,
    IS_DELETED      BIT                                          NOT NULL,
    CREATED_AT      DATETIME(6)                                  NOT NULL,
    CREATED_BY      BIGINT                                       NOT NULL,
    UPDATED_AT      DATETIME(6)                                  NOT NULL,
    UPDATED_BY      BIGINT                                       NOT NULL,
    DELETED_AT      DATETIME(6)                                  NULL,
    DELETED_BY      BIGINT                                       NULL,
    VERSION         INT                                          NOT NULL
);

