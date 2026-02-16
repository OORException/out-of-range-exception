package br.edu.iff.ccc.webdev.service.admin;

import br.edu.iff.ccc.webdev.model.entity.Category;

public interface AdminCategoryService {
    Category create(String name, String description);
    Category update(Long categoryId, String name, String description);
    void delete(Long categoryId);
}
