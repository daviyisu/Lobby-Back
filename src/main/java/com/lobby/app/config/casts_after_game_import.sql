ALTER TABLE game
    ALTER COLUMN platforms
        SET DATA TYPE integer[]
        USING screenshots::integer[];

ALTER TABLE game
    ALTER COLUMN parent_game
        SET DATA TYPE integer
        USING parent_game::integer;

ALTER TABLE game
    ALTER COLUMN involved_companies
        SET DATA TYPE integer[]
        USING involved_companies::integer[];

ALTER TABLE game
    ALTER COLUMN genres
        SET DATA TYPE integer[]
        USING genres::integer[];

ALTER TABLE game
    ALTER COLUMN first_release_date
        SET DATA TYPE timestamp(6) without time zone
        USING first_release_date::timestamp(6) without time zone;

ALTER TABLE game
    ALTER COLUMN created_at
        SET DATA TYPE timestamp(6) without time zone
        USING created_at::timestamp(6) without time zone;

ALTER TABLE game
    ALTER COLUMN cover
        SET DATA TYPE integer
        USING cover::integer;

ALTER TABLE game
    ALTER COLUMN category
        SET DATA TYPE integer
        USING category::integer;

ALTER TABLE game
    ALTER COLUMN artworks
        SET DATA TYPE integer[]
        USING artworks::integer[];

ALTER TABLE game
    ALTER COLUMN aggregated_rating
        SET DATA TYPE decimal
        USING aggregated_rating::decimal;

ALTER TABLE game
    ALTER COLUMN videos
        SET DATA TYPE integer[]
        USING videos::integer[];

ALTER TABLE game
    ALTER COLUMN screenshots
        SET DATA TYPE integer[]
        USING screenshots::integer[];

ALTER TABLE game
    ADD CONSTRAINT game_pk
        PRIMARY KEY (id);
