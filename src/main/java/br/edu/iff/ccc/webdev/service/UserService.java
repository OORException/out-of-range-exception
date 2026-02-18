package br.edu.iff.ccc.webdev.service;

import br.edu.iff.ccc.webdev.dto.request.UpdateUserProfileRequest;
import br.edu.iff.ccc.webdev.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse getById(Long userId);
    List<UserResponse> listAll();
    UserResponse updateProfile(Long userId, UpdateUserProfileRequest request);
}
