package com.moneyflow.backend.budget;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

public record MonthlyBudgetAlertsResponse(
		YearMonth month,
		BigDecimal monthlyIncome,
		List<CategoryBudgetAlert> alerts) {
}
