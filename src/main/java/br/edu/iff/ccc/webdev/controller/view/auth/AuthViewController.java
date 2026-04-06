package br.edu.iff.ccc.webdev.controller.view.auth;

import br.edu.iff.ccc.webdev.dto.request.auth.LoginRequest;
import br.edu.iff.ccc.webdev.dto.request.auth.RegisterRequest;
import br.edu.iff.ccc.webdev.dto.response.auth.AuthResponse;
import br.edu.iff.ccc.webdev.dto.view.form.LoginForm;
import br.edu.iff.ccc.webdev.dto.view.form.RegisterForm;
import br.edu.iff.ccc.webdev.service.auth.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Duration;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthViewController {

    private final AuthService authService;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;

    @GetMapping("/login")
    public String loginPage(Model model) {
        if (!model.containsAttribute("loginForm")) {
            model.addAttribute("loginForm", new LoginForm());
        }
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginForm") LoginForm form,
                        BindingResult result,
                        Model model,
                        HttpServletResponse response,
                        RedirectAttributes attrs) {
        if (result.hasErrors()) {
            return "auth/login";
        }
        try {
            AuthResponse auth = authService.login(new LoginRequest(form.getEmail(), form.getPassword()));
            setJwtCookie(response, auth.token());
            return "redirect:/home";
        } catch (Exception e) {
            model.addAttribute("loginForm", form);
            model.addAttribute("errorMessage", "E-mail ou senha inválidos.");
            return "auth/login";
        }
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        if (!model.containsAttribute("registerForm")) {
            model.addAttribute("registerForm", new RegisterForm());
        }
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerForm") RegisterForm form,
                           BindingResult result,
                           Model model,
                           HttpServletResponse response,
                           RedirectAttributes attrs) {
        if (result.hasErrors()) {
            return "auth/register";
        }
        try {
            AuthResponse auth = authService.register(
                    new RegisterRequest(form.getUsername(), form.getFullName(), form.getEmail(), form.getPassword())
            );
            setJwtCookie(response, auth.token());
            attrs.addFlashAttribute("successMessage", "Conta criada com sucesso! Bem-vindo(a), " + auth.username() + "!");
            return "redirect:/home";
        } catch (Exception e) {
            model.addAttribute("registerForm", form);
            model.addAttribute("errorMessage", e.getMessage());
            return "auth/register";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        try {
            authService.logout();
        } catch (Exception ignored) {
        }
        clearJwtCookie(response);
        return "redirect:/home";
    }

    private void setJwtCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofMillis(jwtExpiration))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private void clearJwtCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ZERO)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
