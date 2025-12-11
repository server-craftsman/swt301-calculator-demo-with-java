package com.fptu.swt301.demo.lab2.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class cho ValidationException
 * Test tất cả các constructors và methods để đạt coverage cao hơn
 */
@DisplayName("ValidationException Tests")
public class ValidationExceptionTest {

    @Test
    @DisplayName("Test constructor with single message")
    void testConstructorWithSingleMessage() {
        String message = "Body weight cannot be less than 0.1 kg";
        ValidationException exception = new ValidationException(message);

        assertEquals(message, exception.getMessage());
        assertEquals(1, exception.getErrorCount());
        assertEquals(1, exception.getErrors().size());
        assertEquals(message, exception.getErrors().get(0));
    }

    @Test
    @DisplayName("Test constructor with list of errors")
    void testConstructorWithListOfErrors() {
        List<String> errors = Arrays.asList(
                "Body weight cannot be less than 0.1 kg",
                "Duration must be greater than 0 minutes");
        ValidationException exception = new ValidationException(errors);

        assertEquals(2, exception.getErrorCount());
        assertEquals(2, exception.getErrors().size());
        assertEquals("Body weight cannot be less than 0.1 kg", exception.getErrors().get(0));
        assertEquals("Duration must be greater than 0 minutes", exception.getErrors().get(1));
        assertTrue(exception.getMessage().contains("2 errors"));
    }

    @Test
    @DisplayName("Test constructor with null list")
    void testConstructorWithNullList() {
        ValidationException exception = new ValidationException((List<String>) null);

        assertTrue(exception.getMessage().contains("Validation failed"));
        assertEquals(0, exception.getErrorCount());
        assertTrue(exception.getErrors().isEmpty());
    }

    @Test
    @DisplayName("Test constructor with empty list")
    void testConstructorWithEmptyList() {
        ValidationException exception = new ValidationException(new ArrayList<>());

        assertTrue(exception.getMessage().contains("Validation failed"));
        assertEquals(0, exception.getErrorCount());
        assertTrue(exception.getErrors().isEmpty());
    }

    @Test
    @DisplayName("Test getErrors returns unmodifiable list")
    void testGetErrorsUnmodifiable() {
        List<String> errors = Arrays.asList("Error 1", "Error 2");
        ValidationException exception = new ValidationException(errors);

        List<String> returnedErrors = exception.getErrors();

        // Should throw UnsupportedOperationException when trying to modify
        assertThrows(UnsupportedOperationException.class, () -> {
            returnedErrors.add("Error 3");
        });

        assertThrows(UnsupportedOperationException.class, () -> {
            returnedErrors.remove(0);
        });
    }

    @Test
    @DisplayName("Test getMessage with single error")
    void testGetMessageSingleError() {
        String message = "Body weight cannot be less than 0.1 kg";
        ValidationException exception = new ValidationException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    @DisplayName("Test getMessage with multiple errors")
    void testGetMessageMultipleErrors() {
        List<String> errors = Arrays.asList(
                "Error 1",
                "Error 2",
                "Error 3");
        ValidationException exception = new ValidationException(errors);

        String message = exception.getMessage();
        assertTrue(message.contains("3 errors"));
        assertTrue(message.contains("Error 1"));
        assertTrue(message.contains("Error 2"));
        assertTrue(message.contains("Error 3"));
    }

    @Test
    @DisplayName("Test exception is instance of RuntimeException")
    void testExceptionType() {
        ValidationException exception = new ValidationException("Test error");
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Test exception can be thrown and caught")
    void testExceptionThrowing() {
        assertThrows(ValidationException.class, () -> {
            throw new ValidationException("Test error");
        });

        try {
            throw new ValidationException("Test error");
        } catch (ValidationException e) {
            assertEquals("Test error", e.getMessage());
            assertEquals(1, e.getErrorCount());
        }
    }

    @Test
    @DisplayName("Test exception with many errors")
    void testExceptionWithManyErrors() {
        List<String> errors = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            errors.add("Error " + i);
        }
        ValidationException exception = new ValidationException(errors);

        assertEquals(10, exception.getErrorCount());
        assertEquals(10, exception.getErrors().size());
        assertTrue(exception.getMessage().contains("10 errors"));
    }
}
