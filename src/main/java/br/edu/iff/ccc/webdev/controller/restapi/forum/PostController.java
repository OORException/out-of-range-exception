package br.edu.iff.ccc.webdev.controller.restapi.forum;

import br.edu.iff.ccc.webdev.dto.request.forum.CreatePostRequest;
import br.edu.iff.ccc.webdev.dto.request.forum.UpdatePostRequest;
import br.edu.iff.ccc.webdev.dto.response.forum.PostResponse;
import br.edu.iff.ccc.webdev.exception.NotFoundException;
import br.edu.iff.ccc.webdev.model.entity.Post;
import br.edu.iff.ccc.webdev.repository.PostRepository;
import br.edu.iff.ccc.webdev.service.forum.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponse create(@Valid @RequestBody CreatePostRequest request) {
        return postService.create(request);
    }

    @GetMapping("/{postId}")
    public PostResponse getById(@PathVariable Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + postId));
        return toResponse(post);
    }

    @GetMapping("/topic/{topicId}")
    public List<PostResponse> getByTopic(@PathVariable Long topicId) {
        return postRepository.findByTopicIdOrderByCreatedAtAsc(topicId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{postId}")
    public PostResponse edit(@PathVariable Long postId, @Valid @RequestBody UpdatePostRequest request) {
        return postService.edit(postId, request.content());
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }

    private PostResponse toResponse(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTopic().getId(),
                post.getAuthor().getId(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
