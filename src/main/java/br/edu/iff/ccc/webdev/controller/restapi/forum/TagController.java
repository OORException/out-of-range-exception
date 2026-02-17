package br.edu.iff.ccc.webdev.controller.restapi.forum;

import br.edu.iff.ccc.webdev.dto.response.forum.TagResponse;
import br.edu.iff.ccc.webdev.model.entity.Tag;
import br.edu.iff.ccc.webdev.service.forum.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public List<TagResponse> listAll() {
        return tagService.listAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{tagId}")
    public TagResponse getById(@PathVariable Long tagId) {
        Tag tag = tagService.getById(tagId);
        return toResponse(tag);
    }

    private TagResponse toResponse(Tag tag) {
        long topicCount = tag.getTopics() != null ? tag.getTopics().size() : 0;
        return new TagResponse(
                tag.getId(),
                tag.getName(),
                topicCount
        );
    }
}
