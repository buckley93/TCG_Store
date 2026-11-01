--liquibase formatted sql

--changeset tcg:003-inventory_items-01
CREATE TABLE inventory_items (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES products(id),
    item_condition VARCHAR(10) CHECK (item_condition IN ('NM', 'LP', 'MP', 'HP', 'DMG')),
    grader VARCHAR(10) CHECK (grader IN ('PSA', 'BGS', 'CGC', 'TAG')),
    grade numeric (3, 1),
    cert_number VARCHAR(50),
    black_label BOOLEAN,
    price_cents BIGINT NOT NULL CHECK (price_cents >= 0),
    quantity INT NOT NULL CHECK (quantity >= 0),
    status VARCHAR(20) CHECK (status IN ('AVAILABLE', 'RESERVED', 'SOLD')) DEFAULT 'AVAILABLE' NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,

    CONSTRAINT chk_slab_fields CHECK (
        (grader IS NULL AND grade IS NULL AND cert_number IS NULL)
        OR (grader IS NOT NULL AND grade IS NOT NULL AND cert_number IS NOT NULL)
    ),

    CONSTRAINT chk_black_label CHECK (
        black_label = false or (grader = 'BGS' and grade = 10)
    )
);

--changeset tcg:003-inventory_items-unique_cert-01
CREATE UNIQUE INDEX uq_inventory_cert
on inventory_items(grader, cert_number)
where cert_number IS NOT NULL;

--changeset tcg:003-inventory_items-product_status-01
CREATE INDEX idx_inventory_product_status ON inventory_items (product_id, status);