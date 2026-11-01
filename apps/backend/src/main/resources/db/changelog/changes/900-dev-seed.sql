--liquibase formatted sql

--changeset tcg:900-dev-seed context:dev
INSERT INTO card_sets (code, name, series, release_date)
VALUES ('sv3', 'Obsidian Flames', 'Scarlet & Violet', '2023-08-11');

-- Card product (one product, many copies)
INSERT INTO products (type, subtype, name, description, set_id, card_number, rarity)
SELECT 'CARD', 'SINGLE_CARD', 'Charizard ex',
       'Charizard ex from Obsidian Flames.',
       id, '125', 'Double Rare'
FROM card_sets WHERE code = 'sv3';

-- Sealed product
INSERT INTO products (type, subtype, name, description, set_id)
SELECT 'SEALED', 'BOOSTER_BOX', 'Obsidian Flames Booster Box',
       'Sealed booster box from Obsidian Flames.',
       id
FROM card_sets WHERE code = 'sv3';

-- Merch (no set)
INSERT INTO products (type, subtype, name, description, attributes)
VALUES ('MERCH', 'PLUSH', 'Charizard Plush',
        'Officially styled Charizard plush, 12 inches tall.',
        '{"heightInches": 12}');

-- Raw copy: 3 in stock
INSERT INTO inventory_items (product_id, item_condition, price_cents, quantity)
SELECT id, 'NM', 1499, 3 FROM products WHERE subtype = 'SINGLE_CARD';

-- PSA 10 slab of the same card
INSERT INTO inventory_items (product_id, grader, grade, cert_number, price_cents, quantity)
SELECT id, 'PSA', 10, '12345678', 24999, 1 FROM products WHERE subtype = 'SINGLE_CARD';

-- BGS 10 and BGS 10 Black Label
INSERT INTO inventory_items (product_id, grader, grade, cert_number, price_cents, quantity)
SELECT id, 'BGS', 10, '0012345678', 59999, 1 FROM products WHERE subtype = 'SINGLE_CARD';

INSERT INTO inventory_items (product_id, grader, grade, cert_number, black_label, price_cents, quantity)
SELECT id, 'BGS', 10, '0087654321', true, 199999, 1 FROM products WHERE subtype = 'SINGLE_CARD';

-- Sealed and merch
INSERT INTO inventory_items (product_id, price_cents, quantity)
SELECT id, 15999, 2 FROM products WHERE subtype = 'BOOSTER_BOX';

INSERT INTO inventory_items (product_id, price_cents, quantity)
SELECT id, 2499, 5 FROM products WHERE subtype = 'PLUSH';

--rollback DELETE FROM inventory_items;
--rollback DELETE FROM products;
--rollback DELETE FROM card_sets WHERE code = 'sv3';