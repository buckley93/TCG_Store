package com.yourco.tcg.inventory;

import com.yourco.tcg.catalog.Product;
import com.yourco.tcg.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;

/**
 * TODO (class):
 * - Sellable unit. Fields: product, condition, grader, grade, certNumber, priceCents, quantity, status.
 * - A slab is one row with quantity 1.
 *
 * Annotations / types: @Entity
 */

@Entity
@Table(name = "inventory_items")
public class InventoryItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_condition", nullable = false, length = 20)
    private Condition condition;

    @Enumerated(EnumType.STRING)
    @Column(name = "grader", length = 10)
    private Grader grader;

    @Column(name = "grade", precision = 3, scale = 1)
    private BigDecimal grade;

    @Column(name = "cert_number", length = 50)
    private String certNumber;

    @Column(name = "black_label", nullable = false)
    private boolean blackLabel;

    @Column(name = "price_cents", nullable = false)
    private long priceCents;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private InventoryStatus status = InventoryStatus.AVAILABLE;

    // Getters and Setters

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Grader getGrader() {
        return grader;
    }

    public void setGrader(Grader grader) {
        this.grader = grader;
    }

    public BigDecimal getGrade() {
        return grade;
    }

    public void setGrade(BigDecimal grade) {
        this.grade = grade;
    }

    public String getCertNumber() {
        return certNumber;
    }

    public void setCertNumber(String certNumber) {
        this.certNumber = certNumber;
    }

    public boolean isBlackLabel() {
        return blackLabel;
    }

    public void setBlackLabel(boolean blackLabel) {
        this.blackLabel = blackLabel;
    }

    public long getPriceCents() {
        return priceCents;
    }

    public void setPriceCents(long priceCents) {
        this.priceCents = priceCents;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public InventoryStatus getStatus() {
        return status;
    }

    public void setStatus(InventoryStatus status) {
        this.status = status;
    }
}

