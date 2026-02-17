package br.edu.iff.ccc.webdev.service.forum.impl;

import br.edu.iff.ccc.webdev.exception.ConflictException;
import br.edu.iff.ccc.webdev.exception.NotFoundException;
import br.edu.iff.ccc.webdev.model.entity.Post;
import br.edu.iff.ccc.webdev.model.entity.User;
import br.edu.iff.ccc.webdev.model.support.Like;
import br.edu.iff.ccc.webdev.repository.LikeRepository;
import br.edu.iff.ccc.webdev.repository.PostRepository;
import br.edu.iff.ccc.webdev.repository.UserRepository;
import br.edu.iff.ccc.webdev.security.SecurityUtils;
import br.edu.iff.ccc.webdev.service.forum.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;

    @Override
    @Transactional
    public void likePost(Long postId) {
        Long userId = securityUtils.getCurrentUserId();

        if (likeRepository.existsByUserIdAndPostId(userId, postId)) {
            throw new ConflictException("You have already liked this post");
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + postId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Like like = Like.builder()
                .user(user)
                .post(post)
                .createdAt(Instant.now())
                .build();

        likeRepository.save(like);
    }

    @Override
    @Transactional
    public void unlikePost(Long postId) {
        Long userId = securityUtils.getCurrentUserId();

        if (!likeRepository.existsByUserIdAndPostId(userId, postId)) {
            throw new NotFoundException("You have not liked this post yet");
        }

        likeRepository.deleteByUserIdAndPostId(userId, postId);
    }
}
