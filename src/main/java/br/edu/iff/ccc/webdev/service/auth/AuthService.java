package br.edu.iff.ccc.webdev.service.auth;

import br.edu.iff.ccc.webdev.dto.request.auth.LoginRequest;
import br.edu.iff.ccc.webdev.dto.request.auth.RegisterRequest;
import br.edu.iff.ccc.webdev.dto.response.auth.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
