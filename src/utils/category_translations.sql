CREATE TABLE CATEGORY_TRANSLATIONS (
    ID NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,
    CATEGORY_ID NUMBER NOT NULL,
    LANGUAGE_ID NUMBER NOT NULL,
    NAME VARCHAR2(255) NOT NULL,
    DESCRIPTION VARCHAR2(4000),
    META_TITLE VARCHAR2(255),
    META_DESCRIPTION VARCHAR2(4000),
    CREATED_AT TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
    UPDATED_AT TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
    CREATED_BY VARCHAR2(255) NOT NULL,
    UPDATED_BY VARCHAR2(255) NOT NULL,
    CONSTRAINT PK_CATEGORY_TRANSLATIONS PRIMARY KEY (ID),
    CONSTRAINT UK_CATEGORY_TRANSLATION UNIQUE (CATEGORY_ID, LANGUAGE_ID),
    CONSTRAINT FK_CATEGORY_TRANSLATIONS_CATEGORY FOREIGN KEY (CATEGORY_ID) REFERENCES CATEGORIES(ID),
    CONSTRAINT FK_CATEGORY_TRANSLATIONS_LANGUAGE FOREIGN KEY (LANGUAGE_ID) REFERENCES LANGUAGES(ID)
);
