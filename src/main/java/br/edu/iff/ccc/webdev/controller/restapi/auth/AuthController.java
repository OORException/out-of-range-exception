package br.edu.iff.ccc.webdev.controller.restapi.auth;

import br.edu.iff.ccc.webdev.dto.request.auth.LoginRequest;
import br.edu.iff.ccc.webdev.dto.request.auth.RegisterRequest;
import br.edu.iff.ccc.webdev.dto.response.auth.AuthResponse;
import br.edu.iff.ccc.webdev.service.auth.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request, HttpServletResponse response) {
        AuthResponse auth = authService.register(request);
        setJwtCookie(response, auth.token());
        return auth;
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        AuthResponse auth = authService.login(request);
        setJwtCookie(response, auth.token());
        return auth;
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout();
        clearServerSession(request);
        SecurityContextHolder.clearContext();
        clearAllRequestCookies(request, response);
        clearFrameworkCookies(response);
        clearJwtCookie(response);
    }

    private void clearServerSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    private void clearAllRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return;
        }

        for (Cookie cookie : cookies) {
            ResponseCookie expired = ResponseCookie.from(cookie.getName(), "")
                    .path("/")
                    .maxAge(Duration.ZERO)
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, expired.toString());
        }
    }

    private void clearFrameworkCookies(HttpServletResponse response) {
        expireCookie(response, "JSESSIONID");
        expireCookie(response, "XSRF-TOKEN");
    }

    private void expireCookie(HttpServletResponse response, String cookieName) {
        ResponseCookie expired = ResponseCookie.from(cookieName, "")
                .path("/")
                .maxAge(Duration.ZERO)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, expired.toString());
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
