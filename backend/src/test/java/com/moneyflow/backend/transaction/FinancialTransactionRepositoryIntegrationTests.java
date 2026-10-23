package com.moneyflow.backend.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.moneyflow.backend.category.Category;
import com.moneyflow.backend.category.CategoryRepository;
import com.moneyflow.backend.category.CategoryType;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class FinancialTransactionRepositoryIntegrationTests {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private FinancialTransactionRepository financialTransactionRepository;

	@Test
	void savesAndLoadsIncomeOrExpenseWithItsCategory() {
		Category category = categoryRepository.saveAndFlush(
				new Category("Salário", CategoryType.INCOME, null));
		FinancialTransaction transaction = new FinancialTransaction("Pagamento mensal", new BigDecimal("3500.00"),
				FinancialTransactionType.INCOME, LocalDate.of(2026, 10, 1), category);

		FinancialTransaction saved = financialTransactionRepository.saveAndFlush(transaction);
		FinancialTransaction loaded = financialTransactionRepository.findById(saved.getId()).orElseThrow();

		assertThat(loaded.getDescription()).isEqualTo("Pagamento mensal");
		assertThat(loaded.getAmount()).isEqualByComparingTo("3500.00");
		assertThat(loaded.getType()).isEqualTo(FinancialTransactionType.INCOME);
		assertThat(loaded.getTransactionDate()).isEqualTo(LocalDate.of(2026, 10, 1));
		assertThat(loaded.getCategory().getId()).isEqualTo(category.getId());
	}

	@Test
	void rejectsTransactionTypeThatDoesNotMatchCategoryType() {
		Category category = categoryRepository.saveAndFlush(
				new Category("Alimentação", CategoryType.EXPENSE, null));
		FinancialTransaction transaction = new FinancialTransaction("Salário", new BigDecimal("100.00"),
				FinancialTransactionType.INCOME, LocalDate.of(2026, 10, 1), category);

		assertThatThrownBy(() -> financialTransactionRepository.saveAndFlush(transaction))
				.isInstanceOf(DataIntegrityViolationException.class);
	}
}
