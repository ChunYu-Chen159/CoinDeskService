---- 切換至schema: coin_schema
SET SCHEMA COIN_SCHEMA;
--
DROP TABLE IF EXISTS coin;

CREATE TABLE coin (
    coin_id VARCHAR(3) NOT NULL,
    coin_name VARCHAR(10) NOT NULL,
    PRIMARY KEY (coin_id)
);

INSERT INTO coin (COIN_ID, COIN_NAME)
VALUES
    ('USD', '美元'),
    ('GBP', '英鎊'),
    ('EUR', '歐元');