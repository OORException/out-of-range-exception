package br.edu.iff.ccc.webdev.controller.view.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.iff.ccc.webdev.model.entity.Category;
import br.edu.iff.ccc.webdev.model.entity.Tag;
import br.edu.iff.ccc.webdev.repository.CategoryRepository;
import br.edu.iff.ccc.webdev.service.forum.TagService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminViewController {

    private final CategoryRepository categoryRepository;
    private final TagService tagService;

    @GetMapping("/categories")
    public String categories(Model model,
                             @RequestParam(required = false) String successMessage,
                             @RequestParam(required = false) String errorMessage) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("successMessage", successMessage);
        model.addAttribute("errorMessage", errorMessage);
        return "admin/categories";
    }

    @GetMapping("/tags")
    public String tags(Model model,
                       @RequestParam(required = false) String successMessage,
                       @RequestParam(required = false) String errorMessage) {
        List<Tag> tags = tagService.listAll();
        model.addAttribute("tags", tags);
        model.addAttribute("successMessage", successMessage);
        model.addAttribute("errorMessage", errorMessage);
        return "admin/tags";
    }

    @GetMapping("/topics")
    public String topics(Model model,
                         @RequestParam(required = false) String successMessage,
                         @RequestParam(required = false) String errorMessage) {
        List<Category> categories = categoryRepository.findAll();
        List<Tag> tags = tagService.listAll();
        model.addAttribute("categories", categories);
        model.addAttribute("tags", tags);
        model.addAttribute("successMessage", successMessage);
        model.addAttribute("errorMessage", errorMessage);
        return "admin/topics";
    }

    @GetMapping("/users")
    public String users(Model model,
                        @RequestParam(required = false) String successMessage,
                        @RequestParam(required = false) String errorMessage) {
        model.addAttribute("successMessage", successMessage);
        model.addAttribute("errorMessage", errorMessage);
        return "admin/users";
    }
}
