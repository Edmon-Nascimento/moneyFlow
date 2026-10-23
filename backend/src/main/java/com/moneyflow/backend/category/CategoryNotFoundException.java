package com.moneyflow.backend.category;

public class CategoryNotFoundException extends RuntimeException {

	public CategoryNotFoundException(Long id) {
		super("Category with id " + id + " was not found.");
	}
}
