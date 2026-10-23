package com.moneyflow.backend.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CategoryRepositoryIntegrationTests {

	@Autowired
	private CategoryRepository categoryRepository;

	@Test
	void savesAndLoadsCategoryFromPostgres() {
		Category category = new Category("Alimentação", CategoryType.EXPENSE, new BigDecimal("25.00"));
		Category savedCategory = categoryRepository.saveAndFlush(category);

		Category loadedCategory = categoryRepository.findById(savedCategory.getId()).orElseThrow();

		assertThat(loadedCategory.getName()).isEqualTo("Alimentação");
		assertThat(loadedCategory.getType()).isEqualTo(CategoryType.EXPENSE);
		assertThat(loadedCategory.getTargetPercentage()).isEqualByComparingTo("25.00");
	}
}
