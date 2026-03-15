package pl.lodz.p.library.adapters.rest.handlers;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Do dat
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Map<String, String> errors = new HashMap<>();

        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException iex = (InvalidFormatException) ex.getCause();

            String fieldName = iex.getPath().stream()
                    .map(ref -> ref.getFieldName())
                    .findFirst()
                    .orElse("pole");

            if (iex.getTargetType().equals(java.time.LocalDateTime.class) ||
                    iex.getTargetType().equals(java.time.LocalDate.class)) {
                errors.put(fieldName, "Niepoprawny format daty. Oczekiwany: yyyy-MM-dd'T'HH:mm:ss (np. 2024-01-01T12:00:00)");
            } else {
                errors.put(fieldName, "Niepoprawny format wartości.");
            }
        } else {
            errors.put("global", "Błąd formatu zapytania JSON.");
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // Obsługa walidacji adnotacjami BeanValidation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // Obsługa unikalności w bazie danych
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateKeyException(DuplicateKeyException ex) {
        Map<String, String> errors = new HashMap<>();
        String message = ex.getMessage();

        if (message != null && message.contains("login")) {
            errors.put("login", "Ten login jest już zajęty.");
        } else if (message != null && message.contains("email")) {
            errors.put("email", "Ten email jest już używany.");
        } else {
            errors.put("global", "Naruszono unikalność danych.");
        }

        return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
    }

    // Obsługa wyjątków biznesowych aplikacji
    @ExceptionHandler(AppBaseException.class)
    public ResponseEntity<Map<String, String>> handleAppBaseException(AppBaseException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getReason());

        if (ex instanceof UserNotFoundException ||
                ex instanceof BookSetNotFoundException ||
                ex instanceof LoanNotFoundException) {
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}