CREATE TABLE inventory_items (
    id              BIGSERIAL PRIMARY KEY,
    product_id      BIGINT       NOT NULL REFERENCES products (id),
    item_condition  VARCHAR(10)
     CHECK (item_condition IN ('NM', 'LP', 'MP', 'HP', 'DMG')),
    grader          VARCHAR(10)
     CHECK (grader IN ('PSA', 'BGS', 'CGC')),
    grade           NUMERIC(3,1),
    cert_number     VARCHAR(50),
    black_label     BOOLEAN      NOT NULL DEFAULT false,
    price_cents     BIGINT       NOT NULL CHECK (price_cents >= 0),
    quantity        INTEGER      NOT NULL CHECK (quantity >= 0),
    status          VARCHAR(20)  NOT NULL DEFAULT 'AVAILABLE'
     CHECK (status IN ('AVAILABLE', 'RESERVED', 'SOLD')),
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ  NOT NULL DEFAULT now(),

    CONSTRAINT chk_slab_fields CHECK (
     (grader IS NULL AND grade IS NULL AND cert_number IS NULL)
         OR (grader IS NOT NULL AND grade IS NOT NULL AND cert_number IS NOT NULL)
     ),

    CONSTRAINT chk_black_label CHECK (
     black_label = false OR (grader = 'BGS' AND grade = 10)
     )
);

CREATE UNIQUE INDEX uq_inventory_cert
    ON inventory_items (grader, cert_number)
    WHERE cert_number IS NOT NULL;

CREATE INDEX idx_inventory_product_status ON inventory_items (product_id, status);