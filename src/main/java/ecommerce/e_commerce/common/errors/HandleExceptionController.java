package ecommerce.e_commerce.common.errors;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

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
    @SuppressWarnings("null")
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolation(DataIntegrityViolationException ex) {

        String property = null;

        //Cannot be null into database
        if (ex.getRootCause() instanceof org.hibernate.PropertyValueException) {
            property = ((org.hibernate.PropertyValueException) ex.getRootCause()).getPropertyName();
            errors.put(property, "The " + property + " files ir required and cannot be null");
        }

        //Already exist into database
        if(ex.getRootCause() instanceof ConstraintViolationException){
            property = ex.getCause().getMessage();

            errors.put("error", property);
        
        }else {//Default error message if not handle
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

    /**
    * Handles the {@link NoSuchElementException} exception globally within the application.
    * This exception is typically thrown when a requested resource (e.g., a role) cannot be found 
    * in the system, usually by ID or name.
    * 
    * @param ex The {@link NoSuchElementException} that is thrown when a role (or other entity) 
    *           cannot be found in the database or data source.
    * 
    * @return A {@link ResponseEntity} containing a map of error details, including the exception 
    *         message, and a {@link HttpStatus} of {@link HttpStatus#NOT_FOUND} (404) indicating 
    *         that the requested resource was not found.
    */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException ex) {
        errors.put("error", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    /**
    * Handles {@link IllegalArgumentException} exceptions globally within the application.
    *
    * This exception is typically thrown when a method receives an argument that is inappropriate 
    * or invalid for its operation. It can occur in cases such as invalid enum values, 
    * incorrect query parameters, or logic validation failures.
    *
    * @param ex The {@link IllegalArgumentException} thrown when an invalid argument is detected in the application logic.
    * 
    * @return A {@link ResponseEntity} containing a map with an error message describing the issue,
    *         and a {@link HttpStatus} of {@link HttpStatus#BAD_REQUEST} (400), indicating that 
    *         the client provided an invalid request parameter or argument.
    */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> IllegalArgumentException(IllegalArgumentException ex) {
        errors.put("error", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
