package com.moneyflow.backend.category;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "categories")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private CategoryType type;

	@Column(name = "target_percentage", precision = 5, scale = 2)
	private BigDecimal targetPercentage;

	protected Category() {
	}

	public Category(String name, CategoryType type, BigDecimal targetPercentage) {
		this.name = name;
		this.type = type;
		this.targetPercentage = targetPercentage;
	}

	public void update(String name, CategoryType type, BigDecimal targetPercentage) {
		this.name = name;
		this.type = type;
		this.targetPercentage = targetPercentage;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public CategoryType getType() {
		return type;
	}

	public BigDecimal getTargetPercentage() {
		return targetPercentage;
	}
}
