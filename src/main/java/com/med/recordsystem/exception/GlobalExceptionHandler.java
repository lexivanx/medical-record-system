package com.med.recordsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public String handleDataNotFoundException(DataNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/data-not-found";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException() {
        return "redirect:/access-denied";
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}