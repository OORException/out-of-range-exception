package br.edu.iff.ccc.webdev.controller.restapi.forum;

import br.edu.iff.ccc.webdev.repository.LikeRepository;
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

    // TODO: Add check if user liked post when authentication is implemented
    // @GetMapping("/check")
    // public boolean isLikedByCurrentUser(@PathVariable Long postId) { ... }
}
