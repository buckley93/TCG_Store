-- TODO: Create tables: card_sets, products (+ indexes, pg_trgm extension for name search)
-- Never edit this file after it has been applied. Add a new migration instead.

Create extension if not exists pg_trgm;

Create Table card_sets (
    id BigSerial primary key,
    code varchar(20) not null,
    name varchar(200) not null,
    series varchar(100) not null,
    release_date date not null,
    created_at TIMESTAMPTZ not null default now(),
    updated_at TIMESTAMPTZ not null default now()
);

Create Table products (
    id BigSerial primary key,
    type varchar(20) not null check ( type in ('CARD', 'SEALED', 'MERCH')),
    subtype varchar(50),
    name varchar(300) not null,
    description varchar(2000),
    set_id BigInt REFERENCES card_sets(id),
    card_number varchar(30),
    rarity varchar(100),
    image_url varchar(500),
    attributes jsonb not null default '{}'::jsonb,
    created_at TIMESTAMPTZ not null default now(),
    updated_at TIMESTAMPTZ not null default now()
);

Create INDEX idx_products_type ON products(type);
Create INDEX idx_products_set_id ON products(set_id);
Create INDEX idx_products_card_number ON products(card_number);
Create INDEX idx_products_name_trgm ON products USING GIN (name gin_trgm_ops);