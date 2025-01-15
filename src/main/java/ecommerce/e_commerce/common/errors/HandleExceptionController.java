package ecommerce.e_commerce.common.errors;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

//Tag global, it handle error in the all endpoint
@RestControllerAdvice 
public class HandleExceptionController {
    
    private Map<String, Object> errors = new HashMap<>();

    /**
     * Handles data integrity violation exceptions that occur during database interactions.
     * This handler captures errors related to database constraints, such as properties that cannot be null,
     * duplicate keys, and other data integrity violations.
     * 
     * @param ex The exception of type {@link DataIntegrityViolationException} containing the details of the database error.
     * @return An HTTP response with a map of errors in the body, with a 400 (Bad Request) status code,
     *         indicating a data integrity violation occurred.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolation(DataIntegrityViolationException ex) {

        String property = null;

        //NOTE: Cannot be null into database
        if (ex.getRootCause() instanceof org.hibernate.PropertyValueException) {
            property = ((org.hibernate.PropertyValueException) ex.getRootCause()).getPropertyName();
            errors.put(property, "The " + property + " files ir required and cannot be null");
        }

        //NOTE: Already exist into database
        if(ex.getRootCause() instanceof ConstraintViolationException){
            property = ex.getCause().getMessage();

            errors.put("error", property);
        
        }else {//NOTE: Default error message if not handle
            errors.put("error", "Data integrity violations: " + ex.getRootCause().getMessage());
        }
        
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }


    /**
     * Handles validation exceptions for method arguments annotated with @NotNull, @NotBlank, etc.
     * This handler captures errors from invalid request parameters and provides a response with a generic error message.
     * 
     * @param ex The exception of type {@link MethodArgumentNotValidException} containing validation errors for the method arguments.
     * @return An HTTP response with a generic error message in the body, along with the field-specific validation messages.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        errors.put("error", fieldErrors);

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
