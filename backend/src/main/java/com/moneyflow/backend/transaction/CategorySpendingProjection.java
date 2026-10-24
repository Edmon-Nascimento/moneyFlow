package com.moneyflow.backend.transaction;

import java.math.BigDecimal;

public interface CategorySpendingProjection {
	Long getCategoryId();

	String getCategoryName();

	BigDecimal getTargetPercentage();

	BigDecimal getSpentAmount();
}
