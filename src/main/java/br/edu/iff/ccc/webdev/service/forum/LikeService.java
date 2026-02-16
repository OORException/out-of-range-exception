package br.edu.iff.ccc.webdev.service.forum;

public interface LikeService {
    void likePost(Long postId);
    void unlikePost(Long postId);
}
