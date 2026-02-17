package br.edu.iff.ccc.webdev.controller.restapi.admin;

import br.edu.iff.ccc.webdev.dto.request.admin.CreateTagRequest;
import br.edu.iff.ccc.webdev.dto.request.admin.UpdateTagRequest;
import br.edu.iff.ccc.webdev.model.entity.Tag;
import br.edu.iff.ccc.webdev.service.admin.AdminTagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/tags")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminTagController {

    private final AdminTagService adminTagService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tag create(@Valid @RequestBody CreateTagRequest request) {
        return adminTagService.create(request.name());
    }

    @PutMapping("/{tagId}")
    public Tag update(@PathVariable Long tagId, @Valid @RequestBody UpdateTagRequest request) {
        return adminTagService.update(tagId, request.name());
    }

    @DeleteMapping("/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long tagId) {
        adminTagService.delete(tagId);
    }
}
