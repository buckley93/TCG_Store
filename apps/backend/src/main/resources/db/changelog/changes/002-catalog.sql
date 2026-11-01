--liquibase formatted sql

--changeset tcg:002-catalog

CREATE EXTENSION IF NOT EXISTS pg_trgm;
   create table card_sets (
       id BIGSERIAL primary key,
       code varchar(20) NOT NULL UNIQUE,
       name varchar(200) not null,
       series varchar(100),
       release_date date,
       created_at timestamp default current_timestamp,
       updated_at timestamp default current_timestamp
   );

    create table products (
        id BIGSERIAL primary key,
        set_id BIGINT references card_sets(id),
        type varchar(20) not null CHECK ( type in ('CARD','SEALED','MERCH','OTHER')),
        subtype varchar(30) not null,
        name varchar(200) not null,
        description varchar(2000) not null,
        card_number varchar(30),
        rarity varchar(30),
        image_url varchar(500),
        attributes JSONB not null default '{}'::jsonb,
        created_at timestamptz default current_timestamp,
        updated_at timestamptz default current_timestamp,

        CONSTRAINT chk_card_fields CHECK (
            (type = 'CARD' AND card_number IS NOT NULL AND rarity IS NOT NULL)
                OR (type <> 'CARD' AND card_number IS NULL AND rarity IS NULL)
            )
   );

CREATE INDEX idx_products_type ON products(type);
CREATE INDEX idx_products_set_id ON products(set_id);
CREATE INDEX idx_products_subtype ON products(subtype);
CREATE INDEX idx_card_number on products (card_number);
CREATE INDEX idx_products_name_trgm ON products USING GIN (name gin_trgm_ops);

--rollback DROP TABLE products;
--rollback DROP TABLE card_sets;