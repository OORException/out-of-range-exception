package br.edu.iff.ccc.webdev.controller.restapi;

import br.edu.iff.ccc.webdev.dto.request.UpdateUserProfileRequest;
import br.edu.iff.ccc.webdev.dto.response.UserResponse;
import br.edu.iff.ccc.webdev.security.SecurityUtils;
import br.edu.iff.ccc.webdev.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SecurityUtils securityUtils;

    @GetMapping
    public List<UserResponse> listAll() {
        return userService.listAll();
    }

    @GetMapping("/{userId}")
    public UserResponse getById(@PathVariable Long userId) {
        return userService.getById(userId);
    }

    @PatchMapping("/me")
    public UserResponse updateMyProfile(@Valid @RequestBody UpdateUserProfileRequest request) {
        Long userId = securityUtils.getCurrentUserId();
        return userService.updateProfile(userId, request);
    }
}
