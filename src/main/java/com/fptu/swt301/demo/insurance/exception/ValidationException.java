package com.fptu.swt301.demo.insurance.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * Exception chứa nhiều validation errors cùng lúc
 * Thay vì báo từng lỗi một, thu thập tất cả lỗi và báo cùng lúc
 */
public class ValidationException extends IllegalArgumentException {
    private final List<String> errors;

    public ValidationException(List<String> errors) {
        super(formatErrorMessage(errors));
        this.errors = new ArrayList<>(errors);
    }

    public ValidationException(String singleError) {
        super(singleError);
        this.errors = new ArrayList<>();
        this.errors.add(singleError);
    }

    public List<String> getErrors() {
        return new ArrayList<>(errors);
    }

    public int getErrorCount() {
        return errors.size();
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    private static String formatErrorMessage(List<String> errors) {
        if (errors == null || errors.isEmpty()) {
            return "Validation failed";
        }
        if (errors.size() == 1) {
            return errors.get(0);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Validation failed with ").append(errors.size()).append(" error(s):\n");
        for (int i = 0; i < errors.size(); i++) {
            sb.append("  ").append(i + 1).append(". ").append(errors.get(i));
            if (i < errors.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return getMessage();
    }
}

