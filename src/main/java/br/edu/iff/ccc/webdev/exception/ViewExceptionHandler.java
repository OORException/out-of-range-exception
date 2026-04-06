package br.edu.iff.ccc.webdev.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice(basePackages = "br.edu.iff.ccc.webdev.controller.view")
public class ViewExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(NotFoundException ex, Model model) {
        log.warn("View 404: {}", ex.getMessage());
        model.addAttribute("message", ex.getMessage());
        return "error/404";
    }

    @ExceptionHandler({ForbiddenException.class, AccessDeniedException.class})
    public String handleForbidden(Exception ex, Model model) {
        log.warn("View 403: {}", ex.getMessage());
        model.addAttribute("message", "Você não tem permissão para acessar este recurso.");
        return "error/403";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneral(Exception ex, Model model) {
        log.error("View 500: ", ex);
        model.addAttribute("message", "Ocorreu um erro interno. Por favor, tente novamente mais tarde.");
        return "error/500";
    }
}
