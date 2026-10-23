package com.moneyflow.backend.category;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
		@NotBlank(message = "must not be blank")
		@Size(max = 100, message = "must be at most 100 characters")
		String name,

		@NotNull(message = "must be provided")
		CategoryType type,

		@DecimalMin(value = "0.00", message = "must be at least 0")
		@DecimalMax(value = "100.00", message = "must be at most 100")
		@Digits(integer = 3, fraction = 2, message = "must have at most 2 decimal places")
		BigDecimal targetPercentage) {
}
