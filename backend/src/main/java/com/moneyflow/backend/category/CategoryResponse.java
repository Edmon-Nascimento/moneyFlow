package com.moneyflow.backend.category;

import java.math.BigDecimal;

public record CategoryResponse(Long id, String name, CategoryType type, BigDecimal targetPercentage) {

	public static CategoryResponse from(Category category) {
		return new CategoryResponse(category.getId(), category.getName(), category.getType(),
				category.getTargetPercentage());
	}
}
