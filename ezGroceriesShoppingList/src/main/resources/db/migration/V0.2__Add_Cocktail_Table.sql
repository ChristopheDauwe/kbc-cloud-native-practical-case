CREATE TABLE COCKTAIL
(
    ID          UUID PRIMARY KEY,
    ID_DRINK        varchar(255) UNIQUE,
    NAME        varchar(255),
    INGREDIENTS  varchar(255)
);
