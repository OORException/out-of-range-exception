package br.edu.iff.ccc.webdev.controller.restapi;

import br.edu.iff.ccc.webdev.dto.response.UserResponse;
import br.edu.iff.ccc.webdev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponse> listAll() {
        return userService.listAll();
    }

    @GetMapping("/{userId}")
    public UserResponse getById(@PathVariable Long userId) {
        return userService.getById(userId);
    }
}
