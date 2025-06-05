package br.com.exercicio4.infra.exception;

import br.com.exercicio4.domain.ValidationException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    // 404 - Entidade não encontrada
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleError404(EntityNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "erro", "Recurso não encontrado",
                        "mensagem", ex.getMessage()
                ));
    }

    // 400 - Validação de dados
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DataValidationError>> handleError400(MethodArgumentNotValidException ex) {
        var errors = ex.getFieldErrors();
        return ResponseEntity
                .badRequest()
                .body(errors.stream().map(DataValidationError::new).toList());
    }

    // 400 - Erro enum inválido (ex: enum inexistente no JSON)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleErrorEnum400(HttpMessageNotReadableException ex) {
        var message = ex.getMostSpecificCause().getMessage();
        return ResponseEntity.badRequest().body(Map.of("erro", message));
    }

    // 400 - Erros de regra de negócio
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> treatErrorBusinessRule(ValidationException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    // 400 - Violação de integridade (duplicidade, constraints, etc.)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> treatIntegrityViolationError(DataIntegrityViolationException ex) {
        String message = extractMessageConstraint(ex);
        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "erro", "Violação de integridade",
                        "mensagem", message
                ));
    }

    // 500 - Erro interno inesperado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleError500(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno: " + ex.getLocalizedMessage());
    }

    // Extrair mensagem mais específica de erro de constraint
    private String extractMessageConstraint(DataIntegrityViolationException ex) {
        Throwable cause = ex.getMostSpecificCause();
        return cause != null ? cause.getMessage() : "Violação de restrição de dados";
    }

    // Record para resposta limpa de erro de validação
    private record DataValidationError(String field, String message) {
        public DataValidationError(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }
}
