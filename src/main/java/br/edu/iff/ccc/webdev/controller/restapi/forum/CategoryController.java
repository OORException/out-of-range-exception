package br.edu.iff.ccc.webdev.controller.restapi.forum;

import br.edu.iff.ccc.webdev.dto.response.forum.CategoryResponse;
import br.edu.iff.ccc.webdev.exception.NotFoundException;
import br.edu.iff.ccc.webdev.model.entity.Category;
import br.edu.iff.ccc.webdev.repository.CategoryRepository;
import br.edu.iff.ccc.webdev.repository.TopicRepository;
import br.edu.iff.ccc.webdev.service.forum.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final TopicRepository topicRepository;

    @GetMapping
    public List<CategoryResponse> list() {
        return categoryService.list().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{categoryId}")
    public CategoryResponse getById(@PathVariable Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found with id: " + categoryId));
        return toResponse(category);
    }

    private CategoryResponse toResponse(Category category) {
        long topicCount = topicRepository.countByCategoryId(category.getId());
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                topicCount
        );
    }
}
