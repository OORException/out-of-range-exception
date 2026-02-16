package br.edu.iff.ccc.webdev.service.forum;

import br.edu.iff.ccc.webdev.dto.request.forum.CreatePostRequest;
import br.edu.iff.ccc.webdev.dto.response.forum.PostResponse;

public interface PostService {
    PostResponse create(CreatePostRequest request);
    PostResponse edit(Long postId, String content);
    void delete(Long postId);
}
