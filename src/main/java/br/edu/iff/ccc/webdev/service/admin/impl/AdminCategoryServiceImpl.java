package br.edu.iff.ccc.webdev.service.admin.impl;

import br.edu.iff.ccc.webdev.exception.ConflictException;
import br.edu.iff.ccc.webdev.exception.NotFoundException;
import br.edu.iff.ccc.webdev.model.entity.Category;
import br.edu.iff.ccc.webdev.repository.CategoryRepository;
import br.edu.iff.ccc.webdev.service.admin.AdminCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminCategoryServiceImpl implements AdminCategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Category create(String name, String description) {
        if (categoryRepository.existsByName(name)) {
            throw new ConflictException("A category with this name already exists: " + name);
        }

        Category category = Category.builder()
                .name(name)
                .description(description)
                .build();

        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category update(Long categoryId, String name, String description) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found with id: " + categoryId));

        if (!category.getName().equals(name) && categoryRepository.existsByName(name)) {
            throw new ConflictException("A category with this name already exists: " + name);
        }

        category.rename(name);
        category.changeDescription(description);

        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void delete(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new NotFoundException("Category not found with id: " + categoryId);
        }

        categoryRepository.deleteById(categoryId);
    }
}
