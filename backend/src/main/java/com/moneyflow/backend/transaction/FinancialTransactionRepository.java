package com.moneyflow.backend.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, Long> {

	@Query("""
			select coalesce(sum(transaction.amount), 0)
			from FinancialTransaction transaction
			where transaction.type = :type
				and transaction.transactionDate >= :from
				and transaction.transactionDate < :to
			""")
	BigDecimal sumAmountByTypeAndDateRange(@Param("type") FinancialTransactionType type,
			@Param("from") LocalDate from, @Param("to") LocalDate to);

	@Query("""
			select category.id as categoryId,
				category.name as categoryName,
				category.targetPercentage as targetPercentage,
				sum(transaction.amount) as spentAmount
			from FinancialTransaction transaction
			join transaction.category category
			where transaction.type = :type
				and transaction.transactionDate >= :from
				and transaction.transactionDate < :to
				and category.targetPercentage is not null
			group by category.id, category.name, category.targetPercentage
			order by category.name
			""")
	List<CategorySpendingProjection> findCategorySpendingByTypeAndDateRange(
			@Param("type") FinancialTransactionType type,
			@Param("from") LocalDate from, @Param("to") LocalDate to);
}
