package com.moneyflow.backend.budget;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import com.moneyflow.backend.transaction.CategorySpendingProjection;
import com.moneyflow.backend.transaction.FinancialTransactionRepository;
import com.moneyflow.backend.transaction.FinancialTransactionType;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BudgetAlertService {

	private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

	private final FinancialTransactionRepository transactionRepository;

	public BudgetAlertService(FinancialTransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

	@Transactional(readOnly = true)
	public MonthlyBudgetAlertsResponse getAlerts(YearMonth month) {
		LocalDate from = month.atDay(1);
		LocalDate to = month.plusMonths(1).atDay(1);
		BigDecimal monthlyIncome = transactionRepository.sumAmountByTypeAndDateRange(
				FinancialTransactionType.INCOME, from, to);

		if (monthlyIncome.signum() <= 0) {
			return new MonthlyBudgetAlertsResponse(month, monthlyIncome, List.of());
		}

		List<CategoryBudgetAlert> alerts = transactionRepository.findCategorySpendingByTypeAndDateRange(
				FinancialTransactionType.EXPENSE, from, to).stream()
			.filter(spending -> exceedsTarget(spending, monthlyIncome))
			.map(spending -> toAlert(spending, monthlyIncome))
			.toList();

		return new MonthlyBudgetAlertsResponse(month, monthlyIncome, alerts);
	}

	private boolean exceedsTarget(CategorySpendingProjection spending, BigDecimal monthlyIncome) {
		return spending.getSpentAmount().multiply(ONE_HUNDRED)
				.compareTo(monthlyIncome.multiply(spending.getTargetPercentage())) > 0;
	}

	private CategoryBudgetAlert toAlert(CategorySpendingProjection spending, BigDecimal monthlyIncome) {
		BigDecimal spentPercentage = spending.getSpentAmount().multiply(ONE_HUNDRED)
				.divide(monthlyIncome, 2, RoundingMode.HALF_UP);
		return new CategoryBudgetAlert(spending.getCategoryId(), spending.getCategoryName(),
				spending.getTargetPercentage(), spentPercentage, spending.getSpentAmount());
	}
}
