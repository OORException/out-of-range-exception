package br.edu.iff.ccc.webdev.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(basePackages = "br.edu.iff.ccc.webdev.controller.restapi")
public class ApiExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFound(NotFoundException ex, HttpServletRequest request) {
        log.error("Not Found: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.NOT_FOUND,
                "Resource Not Found",
                ex.getMessage(),
                ex,
                request
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail handleBadRequest(BadRequestException ex, HttpServletRequest request) {
        log.error("Bad Request: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                ex.getMessage(),
                ex,
                request
        );
    }

    @ExceptionHandler(ConflictException.class)
    public ProblemDetail handleConflict(ConflictException ex, HttpServletRequest request) {
        log.error("Conflict: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.CONFLICT,
                "Data Conflict",
                ex.getMessage(),
                ex,
                request
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ProblemDetail handleUnauthorized(UnauthorizedException ex, HttpServletRequest request) {
        log.error("Unauthorized: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.UNAUTHORIZED,
                "Unauthorized",
                ex.getMessage(),
                ex,
                request
        );
    }

    @ExceptionHandler(ForbiddenException.class)
    public ProblemDetail handleForbidden(ForbiddenException ex, HttpServletRequest request) {
        log.error("Forbidden: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.FORBIDDEN,
                "Access Denied",
                ex.getMessage(),
                ex,
                request
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        log.error("Illegal Argument: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Invalid Argument",
                ex.getMessage(),
                ex,
                request
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        String detail = String.join("; ", errors);
        log.error("Validation Error: {}", detail);
        ProblemDetail problemDetail = buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Validation Error",
                detail,
                ex,
                request
        );
        problemDetail.setProperty("errors", errors);
        return problemDetail;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request) {
        log.error("Data Integrity Violation: {}", ex.getMessage());
        String message = "Operation violated database integrity constraint";
        if (ex.getMessage().contains("constraint")) {
            message = "This record already exists or has dependencies that prevent the operation";
        }
        return buildProblemDetail(
                HttpStatus.CONFLICT,
                "Integrity Violation",
                message,
                ex,
                request
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthentication(AuthenticationException ex, HttpServletRequest request) {
        log.error("Authentication failed: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.UNAUTHORIZED,
                "Authentication Failed",
                "Authentication failed. Please check your credentials.",
                ex,
                request
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        log.error("Access denied: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.FORBIDDEN,
                "Access Denied",
                "Access denied. You do not have permission to access this resource.",
                ex,
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("Internal server error: ", ex);
        return buildProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "Internal server error. Please contact the administrator.",
                ex,
                request
        );
    }

    private ProblemDetail buildProblemDetail(HttpStatus status, String title, String detail,
                                             Exception ex, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setProperty("url", request.getRequestURL().toString());
        problemDetail.setProperty(
                "timestamp",
                LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).toString()
        );
        problemDetail.setProperty("status", HttpStatusCode.valueOf(problemDetail.getStatus()).toString());
        problemDetail.setProperty("message", detail);
        problemDetail.setProperty("exception", ex.getClass().getName());
        problemDetail.setProperty("path", request.getRequestURI());
        return problemDetail;
    }
}
