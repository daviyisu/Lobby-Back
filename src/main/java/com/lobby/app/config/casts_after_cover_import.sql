ALTER TABLE cover
    ALTER COLUMN width
        SET DATA TYPE integer
        USING width::integer;

ALTER TABLE cover
    ALTER COLUMN height
        SET DATA TYPE integer
        USING height::integer;

ALTER TABLE cover
    ALTER COLUMN game
        SET DATA TYPE integer
        USING game::integer;
