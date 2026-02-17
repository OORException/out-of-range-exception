package br.edu.iff.ccc.webdev.controller.restapi.admin;

import br.edu.iff.ccc.webdev.dto.request.admin.CreateCategoryRequest;
import br.edu.iff.ccc.webdev.dto.request.admin.UpdateCategoryRequest;
import br.edu.iff.ccc.webdev.model.entity.Category;
import br.edu.iff.ccc.webdev.service.admin.AdminCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/categories")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category create(@Valid @RequestBody CreateCategoryRequest request) {
        return adminCategoryService.create(request.name(), request.description());
    }

    @PutMapping("/{categoryId}")
    public Category update(@PathVariable Long categoryId, @Valid @RequestBody UpdateCategoryRequest request) {
        return adminCategoryService.update(categoryId, request.name(), request.description());
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long categoryId) {
        adminCategoryService.delete(categoryId);
    }
}
