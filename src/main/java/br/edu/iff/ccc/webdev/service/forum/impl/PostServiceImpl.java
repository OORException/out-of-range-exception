package br.edu.iff.ccc.webdev.service.forum.impl;

import br.edu.iff.ccc.webdev.dto.request.forum.CreatePostRequest;
import br.edu.iff.ccc.webdev.dto.response.forum.PostResponse;
import br.edu.iff.ccc.webdev.exception.NotFoundException;
import br.edu.iff.ccc.webdev.model.entity.Post;
import br.edu.iff.ccc.webdev.model.entity.Topic;
import br.edu.iff.ccc.webdev.model.entity.User;
import br.edu.iff.ccc.webdev.repository.PostRepository;
import br.edu.iff.ccc.webdev.repository.TopicRepository;
import br.edu.iff.ccc.webdev.repository.UserRepository;
import br.edu.iff.ccc.webdev.service.forum.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public PostResponse create(CreatePostRequest request) {
        Topic topic = topicRepository.findById(request.topicId())
                .orElseThrow(() -> new NotFoundException("Topic not found with id: " + request.topicId()));

        // TODO: Get actual user from security context - for now use hardcoded ID
        User author = userRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Post post = Post.builder()
                .content(request.content())
                .topic(topic)
                .author(author)
                .createdAt(Instant.now())
                .build();

        post = postRepository.save(post);

        return toResponse(post);
    }

    @Override
    @Transactional
    public PostResponse edit(Long postId, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + postId));

        post.editContent(content);
        post = postRepository.save(post);

        return toResponse(post);
    }

    @Override
    @Transactional
    public void delete(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new NotFoundException("Post not found with id: " + postId);
        }

        postRepository.deleteById(postId);
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
