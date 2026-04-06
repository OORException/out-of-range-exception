package br.edu.iff.ccc.webdev.controller.view;

import br.edu.iff.ccc.webdev.dto.request.UpdateUserProfileRequest;
import br.edu.iff.ccc.webdev.dto.response.UserResponse;
import br.edu.iff.ccc.webdev.dto.view.form.ProfileForm;
import br.edu.iff.ccc.webdev.security.SecurityUtils;
import br.edu.iff.ccc.webdev.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileViewController {

    private final UserService userService;
    private final SecurityUtils securityUtils;

    private static final java.time.format.DateTimeFormatter DATE_FMT =
            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm")
                    .withZone(java.time.ZoneId.systemDefault());

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public String viewProfile(Model model) {
        UserResponse user = userService.getById(securityUtils.getCurrentUserId());
        model.addAttribute("user", user);
        model.addAttribute("createdAtFormatted", user.createdAt() != null ? DATE_FMT.format(user.createdAt()) : "N/A");
        if (!model.containsAttribute("profileForm")) {
            ProfileForm form = new ProfileForm();
            form.setUsername(user.username());
            form.setFullName(user.fullName());
            form.setEmail(user.email());
            model.addAttribute("profileForm", form);
        }
        return "profile";
    }

    @PostMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public String editProfile(@Valid @ModelAttribute("profileForm") ProfileForm form,
                              BindingResult result,
                              Model model,
                              RedirectAttributes attrs) {
        if (result.hasErrors()) {
            Long userId = securityUtils.getCurrentUserId();
            model.addAttribute("user", userService.getById(userId));
            return "profile";
        }
        try {
            Long userId = securityUtils.getCurrentUserId();
            userService.updateProfile(userId, new UpdateUserProfileRequest(
                    form.getUsername(),
                    form.getFullName(),
                    form.getEmail()
            ));
            attrs.addFlashAttribute("successMessage", "Perfil atualizado com sucesso!");
        } catch (Exception e) {
            attrs.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/profile";
    }
}
