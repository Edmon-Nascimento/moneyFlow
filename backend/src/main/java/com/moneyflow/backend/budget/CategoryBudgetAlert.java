package com.moneyflow.backend.budget;

import java.math.BigDecimal;

public record CategoryBudgetAlert(
		Long categoryId,
		String categoryName,
		BigDecimal targetPercentage,
		BigDecimal spentPercentage,
		BigDecimal spentAmount) {
}
