package com.example.MyBookShopApp.controllers.global;

import com.example.MyBookShopApp.errs.EmailExistsException;
import com.example.MyBookShopApp.errs.EmptySearchException;
import com.example.MyBookShopApp.errs.ItemNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmptySearchException.class)
    public String handleEmptySearchException(
            EmptySearchException e,
            RedirectAttributes redirectAttributes
    ) {
        log.warn(e.getLocalizedMessage());
        redirectAttributes.addFlashAttribute("searchError", e);
        return "redirect:/";
    }


    @ExceptionHandler({ItemNotFoundException.class})
    public String handleBooksNotFound(RedirectAttributes attributes) {
        attributes.addFlashAttribute("notFoundError", true);
        return "redirect:/404";
    }


    @ExceptionHandler({AuthenticationException.class})
    public String handleAuthenticationException(RedirectAttributes attributes) {
        attributes.addFlashAttribute("authError", true);
        return "redirect:/signin";
    }

    @ExceptionHandler({EmailExistsException.class})
    public String handleRegWithExistingEmail(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("emailExistsError", true);
        return "redirect:/signup";
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
