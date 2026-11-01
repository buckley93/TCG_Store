package com.yourco.tcg.catalog;

public enum ProductSubType {
    //CARD
    SINGLE_CARD(ProductType.CARD),

    // SEALED
    BOOSTER_PACK(ProductType.SEALED),
    BOOSTER_BOX(ProductType.SEALED),
    ELITE_TRAINER_BOX(ProductType.SEALED),
    COLLECTOR_TIN(ProductType.SEALED),
    BUNDLE(ProductType.SEALED),

    //MERCH
    PLUSH(ProductType.MERCH),
    SLEEVES(ProductType.MERCH),
    PLAYMAT(ProductType.MERCH),
    BINDER(ProductType.MERCH),
    DECK_BOX(ProductType.MERCH);

    private final ProductType parent;

    ProductSubType(ProductType parent) {
        this.parent = parent;
    }

    public ProductType getParent() {
        return parent;
    }

}
