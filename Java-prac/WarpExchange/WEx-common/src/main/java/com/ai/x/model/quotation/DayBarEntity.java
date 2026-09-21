package com.ai.x.model.quotation;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Store bars of second.
 */
@Entity
@Table(name = "day_bars")
public class DayBarEntity extends AbstractBarEntity {
}
