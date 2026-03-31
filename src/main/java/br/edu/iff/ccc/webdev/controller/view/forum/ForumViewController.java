package br.edu.iff.ccc.webdev.controller.view.forum;

import br.edu.iff.ccc.webdev.dto.response.forum.TagResponse;
import br.edu.iff.ccc.webdev.dto.response.forum.TopicResponse;
import br.edu.iff.ccc.webdev.dto.view.PostDetailView;
import br.edu.iff.ccc.webdev.exception.NotFoundException;
import br.edu.iff.ccc.webdev.model.entity.Topic;
import br.edu.iff.ccc.webdev.repository.ChatRepository;
import br.edu.iff.ccc.webdev.repository.LikeRepository;
import br.edu.iff.ccc.webdev.repository.PostRepository;
import br.edu.iff.ccc.webdev.repository.TopicRepository;
import br.edu.iff.ccc.webdev.service.forum.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

    @GetMapping("/topics/new")
    public String createTopic() {
        return "forum/topic-create.html";
    }

    @GetMapping("/topics/{id}")
    @Transactional(readOnly = true)
    public String viewTopic(@PathVariable Long id, Model model) {
        // Records view + validates existence
        TopicResponse topicResponse = topicService.getById(id);

        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Topic not found"));

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
