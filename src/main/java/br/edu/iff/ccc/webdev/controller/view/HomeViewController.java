package br.edu.iff.ccc.webdev.controller.view;

import br.edu.iff.ccc.webdev.dto.response.forum.TopicResponse;
import br.edu.iff.ccc.webdev.dto.view.HomeChatCard;
import br.edu.iff.ccc.webdev.dto.view.HomeTopicCard;
import br.edu.iff.ccc.webdev.repository.CategoryRepository;
import br.edu.iff.ccc.webdev.repository.ChatRepository;
import br.edu.iff.ccc.webdev.repository.PostRepository;
import br.edu.iff.ccc.webdev.service.chat.ChatParticipationService;
import br.edu.iff.ccc.webdev.service.forum.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeViewController {

    private final TopicService topicService;
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final ChatRepository chatRepository;
    private final ChatParticipationService chatParticipationService;

    @GetMapping
    @Transactional(readOnly = true)
    public String paginaPrincipal(Model model) {
        List<TopicResponse> allTopics = topicService.list();

        Map<Long, String> categoryNames = categoryRepository.findAll().stream()
                .collect(Collectors.toMap(c -> c.getId(), c -> c.getName()));

        List<HomeTopicCard> topicCards = allTopics.stream()
                .limit(4)
                .map(t -> toTopicCard(t, categoryNames))
                .toList();

        List<HomeTopicCard> trendingTopics = allTopics.stream()
                .sorted(Comparator.comparingLong(TopicResponse::viewCount).reversed())
                .limit(3)
                .map(t -> toTopicCard(t, categoryNames))
                .toList();

        List<HomeChatCard> chatCards = chatRepository.findByActiveTrue().stream()
                .map(c -> new HomeChatCard(
                        c.getId(),
                        c.getName(),
                        c.getTopic().getTitle(),
                        chatParticipationService.countActiveParticipants(c.getId()),
                        c.getTopic().getId()
                ))
                .toList();

        model.addAttribute("topicCards", topicCards);
        model.addAttribute("trendingTopics", trendingTopics);
        model.addAttribute("chatCards", chatCards);

        return "home.html";
    }

    private HomeTopicCard toTopicCard(TopicResponse t, Map<Long, String> categoryNames) {
        return new HomeTopicCard(
                t.id(),
                t.title(),
                categoryNames.getOrDefault(t.categoryId(), ""),
                postRepository.countByTopicId(t.id()),
                formatRelativeTime(t.lastActivityAt())
        );
    }

    private String formatRelativeTime(Instant instant) {
        Duration duration = Duration.between(instant, Instant.now());
        long minutes = duration.toMinutes();
        if (minutes < 1) return "agora mesmo";
        if (minutes < 60) return "há " + minutes + "min";
        long hours = duration.toHours();
        if (hours < 24) return "há " + hours + "h";
        return "há " + duration.toDays() + "d";
    }
}
