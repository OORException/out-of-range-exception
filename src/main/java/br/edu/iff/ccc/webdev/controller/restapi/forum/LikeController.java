package br.edu.iff.ccc.webdev.controller.restapi.forum;

import br.edu.iff.ccc.webdev.repository.LikeRepository;
import br.edu.iff.ccc.webdev.security.SecurityUtils;
import br.edu.iff.ccc.webdev.service.forum.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts/{postId}/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;
    private final LikeRepository likeRepository;
    private final SecurityUtils securityUtils;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void like(@PathVariable Long postId) {
        likeService.likePost(postId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlike(@PathVariable Long postId) {
        likeService.unlikePost(postId);
    }

    @GetMapping("/count")
    public long getCount(@PathVariable Long postId) {
        return likeRepository.countByPostId(postId);
    }

    @GetMapping("/check")
    public boolean isLikedByCurrentUser(@PathVariable Long postId) {
        Long userId = securityUtils.getCurrentUserId();
        return likeRepository.existsByUserIdAndPostId(userId, postId);
    }
}
