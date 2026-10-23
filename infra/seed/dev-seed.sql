-- Dev seed data. Run once against an empty (freshly migrated) database.

INSERT INTO card_sets (code, name, series, release_date)
VALUES ('sv3', 'Obsidian Flames', 'Scarlet & Violet', '2023-08-11');

-- one product: the card
INSERT INTO products (type, name, set_id, card_number, rarity)
SELECT 'CARD', 'Charizard ex', 1, '125', 'Double Rare' FROM card_sets WHERE code = 'sv3';

-- sealed product
INSERT INTO products (type, subtype, name, set_id)
SELECT 'SEALED', 'BOOSTER_BOX', 'Obsidian Flames Booster Box', id FROM card_sets WHERE code = 'sv3';

-- merch, no set
INSERT INTO products (type, subtype, name, attributes)
VALUES ('MERCH', 'PLUSH', 'Charizard Plush', '{"heightInches": 12}');

-- raw copy: 3 in stock
INSERT INTO inventory_items (product_id, item_condition, price_cents, quantity)
SELECT id, 'NM', 1499, 3 FROM products WHERE type = 'CARD';

-- graded copy of the SAME card: PSA 10
INSERT INTO inventory_items (product_id, grader, grade, cert_number, price_cents, quantity)
SELECT id, 'PSA', 10, '12345678', 24999, 1 FROM products WHERE type = 'CARD';

INSERT INTO inventory_items (product_id, price_cents, quantity)
SELECT id, 15999, 2 FROM products WHERE subtype = 'BOOSTER_BOX';

INSERT INTO inventory_items (product_id, price_cents, quantity)
SELECT id, 2499, 5 FROM products WHERE subtype = 'PLUSH';

SELECT p.type, p.subtype, p.name, i.price_cents, i.quantity, i.grader, i.grade
FROM inventory_items i JOIN products p ON p.id = i.product_id;

INSERT INTO card_sets (code, name, series, release_date)
VALUES ('sv3', 'Obsidian Flames', 'Scarlet & Violet', '2023-08-11')
    ON CONFLICT (code) DO NOTHING;

-- Card product (one product, many copies)
INSERT INTO products (type, subtype, name, set_id, card_number, rarity)
SELECT 'CARD', 'SINGLE_CARD', 'Charizard ex', id, '125', 'Double Rare'
FROM card_sets WHERE code = 'sv3';

-- Sealed product
INSERT INTO products (type, subtype, name, set_id)
SELECT 'SEALED', 'BOOSTER_BOX', 'Obsidian Flames Booster Box', id
FROM card_sets WHERE code = 'sv3';

-- Merch (no set)
INSERT INTO products (type, subtype, name, attributes)
VALUES ('MERCH', 'PLUSH', 'Charizard Plush', '{"heightInches": 12}');

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