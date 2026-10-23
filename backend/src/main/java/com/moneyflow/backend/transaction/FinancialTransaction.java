package com.moneyflow.backend.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.moneyflow.backend.category.Category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "financial_transactions")
public class FinancialTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 200)
	private String description;

	@Column(nullable = false, precision = 14, scale = 2)
	private BigDecimal amount;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private FinancialTransactionType type;

	@Column(name = "transaction_date", nullable = false)
	private LocalDate transactionDate;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	protected FinancialTransaction() {
	}

	public FinancialTransaction(String description, BigDecimal amount, FinancialTransactionType type,
			LocalDate transactionDate, Category category) {
		this.description = description;
		this.amount = amount;
		this.type = type;
		this.transactionDate = transactionDate;
		this.category = category;
	}

	public Long getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public FinancialTransactionType getType() {
		return type;
	}

	public LocalDate getTransactionDate() {
		return transactionDate;
	}

	public Category getCategory() {
		return category;
	}
}
