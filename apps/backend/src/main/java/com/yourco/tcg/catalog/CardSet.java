package com.yourco.tcg.catalog;

import com.yourco.tcg.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import java.time.LocalDate;


/**
 * TODO (class):
 * - Fields: code, name, releaseDate, series.
 *
 * Annotations / types: @Entity
 */

@Entity
@Table(name = "card_sets")
public class CardSet extends BaseEntity {

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 100)
    private String series;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    // Getters and Setters

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
            return name;
        }

    public void setName(String name) {
            this.name = name;
        }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

}
