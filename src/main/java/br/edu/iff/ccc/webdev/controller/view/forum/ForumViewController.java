package br.edu.iff.ccc.webdev.controller.view.forum;

import br.edu.iff.ccc.webdev.dto.request.forum.CreateTopicRequest;
import br.edu.iff.ccc.webdev.dto.response.forum.TagResponse;
import br.edu.iff.ccc.webdev.dto.response.forum.TopicResponse;
import br.edu.iff.ccc.webdev.dto.view.PostDetailView;
import br.edu.iff.ccc.webdev.dto.view.form.CreateTopicForm;
import br.edu.iff.ccc.webdev.exception.NotFoundException;
import br.edu.iff.ccc.webdev.model.entity.Category;
import br.edu.iff.ccc.webdev.model.entity.Tag;
import br.edu.iff.ccc.webdev.model.entity.Topic;
import br.edu.iff.ccc.webdev.repository.CategoryRepository;
import br.edu.iff.ccc.webdev.repository.ChatRepository;
import br.edu.iff.ccc.webdev.repository.LikeRepository;
import br.edu.iff.ccc.webdev.repository.PostRepository;
import br.edu.iff.ccc.webdev.repository.TopicRepository;
import br.edu.iff.ccc.webdev.service.forum.CategoryService;
import br.edu.iff.ccc.webdev.service.forum.TagService;
import br.edu.iff.ccc.webdev.service.forum.TopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/forum")
@RequiredArgsConstructor
public class ForumViewController {

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm").withZone(ZoneId.systemDefault());

    private final TopicService topicService;
    private final TopicRepository topicRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final ChatRepository chatRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    @GetMapping({"", "/"})
    @Transactional(readOnly = true)
    public String forumIndex(@RequestParam(required = false) Long categoryId, Model model) {
        List<TopicResponse> topics = topicService.list();
        List<Category> categories = categoryService.list();

        Map<Long, String> categoryNames = categories.stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));

        List<TopicResponse> filtered = categoryId != null
                ? topics.stream().filter(t -> t.categoryId() != null && t.categoryId().equals(categoryId)).toList()
                : topics;

        Map<Long, Long> postCounts = filtered.stream()
                .collect(Collectors.toMap(
                        TopicResponse::id,
                        t -> postRepository.countByTopicId(t.id())
                ));

        Map<Long, String> lastActivityFormatted = filtered.stream()
                .collect(Collectors.toMap(
                        TopicResponse::id,
                        t -> t.lastActivityAt() != null ? DATE_FMT.format(t.lastActivityAt()) : ""
                ));

        model.addAttribute("topics", filtered);
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("categoryNames", categoryNames);
        model.addAttribute("postCounts", postCounts);
        model.addAttribute("lastActivityFormatted", lastActivityFormatted);
        return "forum/index";
    }

    @GetMapping("/topics/new")
    @Transactional(readOnly = true)
    public String createTopicPage(Model model) {
        model.addAttribute("categories", categoryService.list());
        model.addAttribute("tags", tagService.listAll());
        if (!model.containsAttribute("topicForm")) {
            model.addAttribute("topicForm", new CreateTopicForm());
        }
        return "forum/topic-create";
    }

    @PostMapping("/topics")
    @Transactional
    public String createTopic(@Valid @ModelAttribute("topicForm") CreateTopicForm form,
                              BindingResult result,
                              Model model,
                              RedirectAttributes attrs) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.list());
            model.addAttribute("tags", tagService.listAll());
            return "forum/topic-create";
        }
        try {
            TopicResponse created = topicService.create(new CreateTopicRequest(
                    form.getCategoryId(),
                    form.getTitle(),
                    form.getDescription(),
                    form.getTagIds().isEmpty() ? null : form.getTagIds()
            ));
            attrs.addFlashAttribute("successMessage", "Tópico criado com sucesso!");
            return "redirect:/forum/topics/" + created.id();
        } catch (Exception e) {
            model.addAttribute("categories", categoryService.list());
            model.addAttribute("tags", tagService.listAll());
            model.addAttribute("errorMessage", e.getMessage());
            return "forum/topic-create";
        }
    }

    @GetMapping("/topics/{id}")
    @Transactional(readOnly = true)
    public String viewTopic(@PathVariable Long id, Model model) {
        TopicResponse topicResponse = topicService.getById(id);

        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tópico não encontrado"));

        List<TagResponse> tags = topic.getTags().stream()
                .map(t -> new TagResponse(t.getId(), t.getName(), 0))
                .toList();

        List<PostDetailView> posts = postRepository.findByTopicIdOrderByCreatedAtAsc(id).stream()
                .map(p -> new PostDetailView(
                        p.getId(),
                        p.getAuthor().getId(),
                        p.getAuthor().getUsername(),
                        p.getContent(),
                        formatDate(p.getCreatedAt()),
                        p.getUpdatedAt() != null ? formatDate(p.getUpdatedAt()) : null,
                        likeRepository.countByPostId(p.getId())
                ))
                .toList();

        Long chatId = chatRepository.findByTopicId(id).map(c -> c.getId()).orElse(null);

        model.addAttribute("topic", topicResponse);
        model.addAttribute("categoryName", topic.getCategory().getName());
        model.addAttribute("categoryId", topic.getCategory().getId());
        model.addAttribute("tags", tags);
        model.addAttribute("posts", posts);
        model.addAttribute("chatId", chatId);
        model.addAttribute("createdAt", formatDate(topicResponse.createdAt()));

        return "forum/topic-view.html";
    }

    private String formatDate(Instant instant) {
        return DATE_FMT.format(instant);
    }
}
