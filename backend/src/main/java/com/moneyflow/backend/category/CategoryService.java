package com.moneyflow.backend.category;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryService {

	private final CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Transactional(readOnly = true)
	public List<CategoryResponse> findAll() {
		return categoryRepository.findAll(Sort.by("type").ascending().and(Sort.by("name").ascending()))
				.stream()
				.map(CategoryResponse::from)
				.toList();
	}

	@Transactional(readOnly = true)
	public CategoryResponse findById(Long id) {
		return CategoryResponse.from(getCategory(id));
	}

	public CategoryResponse create(CategoryRequest request) {
		Category category = new Category(request.name().trim(), request.type(), request.targetPercentage());
		return CategoryResponse.from(categoryRepository.save(category));
	}

	public CategoryResponse update(Long id, CategoryRequest request) {
		Category category = getCategory(id);
		category.update(request.name().trim(), request.type(), request.targetPercentage());
		return CategoryResponse.from(categoryRepository.save(category));
	}

	public void delete(Long id) {
		categoryRepository.delete(getCategory(id));
	}

	private Category getCategory(Long id) {
		return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
	}
}
