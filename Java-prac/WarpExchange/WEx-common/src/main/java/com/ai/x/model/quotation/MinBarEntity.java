package com.ai.x.model.quotation;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Store bars of second.
 */
@Entity
@Table(name = "min_bars")
public class MinBarEntity extends AbstractBarEntity {
}
