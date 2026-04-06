package br.edu.iff.ccc.webdev.controller.view.admin;

import br.edu.iff.ccc.webdev.dto.request.forum.CreateTopicRequest;
import br.edu.iff.ccc.webdev.dto.view.form.CategoryForm;
import br.edu.iff.ccc.webdev.dto.view.form.CreateTopicForm;
import br.edu.iff.ccc.webdev.dto.view.form.TagForm;
import br.edu.iff.ccc.webdev.model.entity.Category;
import br.edu.iff.ccc.webdev.model.entity.Tag;
import br.edu.iff.ccc.webdev.repository.CategoryRepository;
import br.edu.iff.ccc.webdev.service.UserService;
import br.edu.iff.ccc.webdev.service.admin.AdminCategoryService;
import br.edu.iff.ccc.webdev.service.admin.AdminTagService;
import br.edu.iff.ccc.webdev.service.forum.TagService;
import br.edu.iff.ccc.webdev.service.forum.TopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminViewController {

    private final CategoryRepository categoryRepository;
    private final TagService tagService;
    private final AdminCategoryService adminCategoryService;
    private final AdminTagService adminTagService;
    private final TopicService topicService;
    private final UserService userService;

    // ─────────────────────────── CATEGORIES ───────────────────────────

    @GetMapping("/categories")
    public String categories(Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        if (!model.containsAttribute("categoryForm")) {
            model.addAttribute("categoryForm", new CategoryForm());
        }
        return "admin/categories";
    }

    @PostMapping("/categories")
    public String createCategory(@Valid @ModelAttribute("categoryForm") CategoryForm form,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes attrs) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            return "admin/categories";
        }
        try {
            adminCategoryService.create(form.getName(), form.getDescription());
            attrs.addFlashAttribute("successMessage", "Categoria \"" + form.getName() + "\" criada com sucesso!");
        } catch (Exception e) {
            attrs.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/categories";
    }

    @PostMapping("/categories/{id}/delete")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes attrs) {
        try {
            adminCategoryService.delete(id);
            attrs.addFlashAttribute("successMessage", "Categoria excluída com sucesso!");
        } catch (Exception e) {
            attrs.addFlashAttribute("errorMessage", "Erro ao excluir categoria: " + e.getMessage());
        }
        return "redirect:/admin/categories";
    }

    // ─────────────────────────── TAGS ───────────────────────────

    @GetMapping("/tags")
    public String tags(Model model) {
        List<Tag> tags = tagService.listAll();
        model.addAttribute("tags", tags);
        if (!model.containsAttribute("tagForm")) {
            model.addAttribute("tagForm", new TagForm());
        }
        return "admin/tags";
    }

    @PostMapping("/tags")
    public String createTag(@Valid @ModelAttribute("tagForm") TagForm form,
                            BindingResult result,
                            Model model,
                            RedirectAttributes attrs) {
        if (result.hasErrors()) {
            model.addAttribute("tags", tagService.listAll());
            return "admin/tags";
        }
        try {
            adminTagService.create(form.getName());
            attrs.addFlashAttribute("successMessage", "Tag \"" + form.getName() + "\" criada com sucesso!");
        } catch (Exception e) {
            attrs.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/tags";
    }

    @PostMapping("/tags/{id}/delete")
    public String deleteTag(@PathVariable Long id, RedirectAttributes attrs) {
        try {
            adminTagService.delete(id);
            attrs.addFlashAttribute("successMessage", "Tag excluída com sucesso!");
        } catch (Exception e) {
            attrs.addFlashAttribute("errorMessage", "Erro ao excluir tag: " + e.getMessage());
        }
        return "redirect:/admin/tags";
    }

    // ─────────────────────────── TOPICS ───────────────────────────

    @GetMapping("/topics")
    public String topics(Model model) {
        List<Category> categories = categoryRepository.findAll();
        List<Tag> tags = tagService.listAll();
        model.addAttribute("categories", categories);
        model.addAttribute("tags", tags);
        if (!model.containsAttribute("topicForm")) {
            model.addAttribute("topicForm", new CreateTopicForm());
        }
        return "admin/topics";
    }

    @PostMapping("/topics")
    public String createTopic(@Valid @ModelAttribute("topicForm") CreateTopicForm form,
                              BindingResult result,
                              Model model,
                              RedirectAttributes attrs) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("tags", tagService.listAll());
            return "admin/topics";
        }
        try {
            topicService.create(new CreateTopicRequest(
                    form.getCategoryId(),
                    form.getTitle(),
                    form.getDescription(),
                    form.getTagIds()
            ));
            attrs.addFlashAttribute("successMessage", "Tópico \"" + form.getTitle() + "\" criado com sucesso!");
        } catch (Exception e) {
            attrs.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/topics";
    }

    @PostMapping("/topics/{id}/delete")
    public String deleteTopic(@PathVariable Long id, RedirectAttributes attrs) {
        try {
            topicService.delete(id);
            attrs.addFlashAttribute("successMessage", "Tópico excluído com sucesso!");
        } catch (Exception e) {
            attrs.addFlashAttribute("errorMessage", "Erro ao excluir tópico: " + e.getMessage());
        }
        return "redirect:/admin/topics";
    }

    // ─────────────────────────── USERS ───────────────────────────

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.listAll());
        return "admin/users";
    }
}
