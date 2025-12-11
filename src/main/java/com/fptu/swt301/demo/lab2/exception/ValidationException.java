package com.fptu.swt301.demo.lab2.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Custom exception cho validation errors
 * Cho phép thu thập nhiều lỗi cùng lúc
 */
public class ValidationException extends RuntimeException {

    private final List<String> errors;

    public ValidationException(String message) {
        super(message);
        this.errors = new ArrayList<>();
        this.errors.add(message);
    }

    public ValidationException(List<String> errors) {
        super(errors != null && !errors.isEmpty() ? errors.get(0) : "Validation failed");
        this.errors = errors != null ? new ArrayList<>(errors) : new ArrayList<>();
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public int getErrorCount() {
        return errors.size();
    }

    @Override
    public String getMessage() {
        if (errors.size() == 1) {
            return errors.get(0);
        }
        return String.format("Validation failed with %d errors: %s", errors.size(), String.join(", ", errors));
    }
}

