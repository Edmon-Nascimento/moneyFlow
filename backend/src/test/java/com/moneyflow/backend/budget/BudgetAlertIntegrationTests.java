package com.moneyflow.backend.budget;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.moneyflow.backend.category.Category;
import com.moneyflow.backend.category.CategoryRepository;
import com.moneyflow.backend.category.CategoryType;
import com.moneyflow.backend.transaction.FinancialTransaction;
import com.moneyflow.backend.transaction.FinancialTransactionRepository;
import com.moneyflow.backend.transaction.FinancialTransactionType;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BudgetAlertIntegrationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private FinancialTransactionRepository transactionRepository;

	@Test
	void reportsOnlyCategoriesThatExceededTheirTargetComparedWithMonthlyIncome() throws Exception {
		Category incomeCategory = categoryRepository.saveAndFlush(
				new Category("Salário", CategoryType.INCOME, null));
		Category exceededCategory = categoryRepository.saveAndFlush(
				new Category("Alimentação", CategoryType.EXPENSE, new BigDecimal("20.00")));
		Category atLimitCategory = categoryRepository.saveAndFlush(
				new Category("Transporte", CategoryType.EXPENSE, new BigDecimal("10.00")));

		saveTransaction("Salário", "4000.00", FinancialTransactionType.INCOME,
				LocalDate.of(2026, 10, 2), incomeCategory);
		saveTransaction("Mercado", "1000.00", FinancialTransactionType.EXPENSE,
				LocalDate.of(2026, 10, 20), exceededCategory);
		saveTransaction("Ônibus", "400.00", FinancialTransactionType.EXPENSE,
				LocalDate.of(2026, 10, 8), atLimitCategory);
		saveTransaction("Cinema", "500.00", FinancialTransactionType.EXPENSE,
				LocalDate.of(2026, 11, 8), exceededCategory);

		mockMvc.perform(get("/api/budget-alerts").param("month", "2026-10"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.month").value("2026-10"))
			.andExpect(jsonPath("$.monthlyIncome").value(4000.00))
			.andExpect(jsonPath("$.alerts.length()").value(1))
			.andExpect(jsonPath("$.alerts[0].categoryName").value("Alimentação"))
			.andExpect(jsonPath("$.alerts[0].targetPercentage").value(20.00))
			.andExpect(jsonPath("$.alerts[0].spentPercentage").value(25.00))
			.andExpect(jsonPath("$.alerts[0].spentAmount").value(1000.00));
	}

	@Test
	void doesNotBlockFurtherTransactionsAfterReportingAnExceededTarget() throws Exception {
		Category incomeCategory = categoryRepository.saveAndFlush(
				new Category("Salário", CategoryType.INCOME, null));
		Category expenseCategory = categoryRepository.saveAndFlush(
				new Category("Lazer", CategoryType.EXPENSE, new BigDecimal("10.00")));
		saveTransaction("Salário", "1000.00", FinancialTransactionType.INCOME,
				LocalDate.of(2026, 10, 2), incomeCategory);
		saveTransaction("Viagem", "200.00", FinancialTransactionType.EXPENSE,
				LocalDate.of(2026, 10, 8), expenseCategory);

		mockMvc.perform(get("/api/budget-alerts").param("month", "2026-10"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.alerts.length()").value(1));

		FinancialTransaction laterTransaction = saveTransaction("Jantar", "50.00",
				FinancialTransactionType.EXPENSE, LocalDate.of(2026, 10, 9), expenseCategory);

		org.assertj.core.api.Assertions.assertThat(transactionRepository.existsById(laterTransaction.getId())).isTrue();
	}

	private FinancialTransaction saveTransaction(String description, String amount,
			FinancialTransactionType type, LocalDate date, Category category) {
		return transactionRepository.saveAndFlush(
				new FinancialTransaction(description, new BigDecimal(amount), type, date, category));
	}
}
