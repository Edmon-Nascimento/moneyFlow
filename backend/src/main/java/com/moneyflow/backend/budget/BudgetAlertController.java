package com.moneyflow.backend.budget;

import java.time.YearMonth;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/budget-alerts")
public class BudgetAlertController {

	private final BudgetAlertService budgetAlertService;

	public BudgetAlertController(BudgetAlertService budgetAlertService) {
		this.budgetAlertService = budgetAlertService;
	}

	@GetMapping
	public MonthlyBudgetAlertsResponse getAlerts(
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
		return budgetAlertService.getAlerts(month == null ? YearMonth.now() : month);
	}
}
