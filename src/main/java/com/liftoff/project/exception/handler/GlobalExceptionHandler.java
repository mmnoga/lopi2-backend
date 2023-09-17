package com.liftoff.project.exception.handler;

import com.liftoff.project.exception.BusinessException;
import com.liftoff.project.exception.TechnicalException;
import com.liftoff.project.exception.cart.EntityNotFoundException;
import com.liftoff.project.exception.cart.TermsNotAcceptedException;
import com.liftoff.project.exception.category.CannotDeleteCategoryException;
import com.liftoff.project.exception.category.InvalidParentCategoryException;
import com.liftoff.project.exception.cookie.CookieNotFoundException;
import com.liftoff.project.exception.product.InvalidDiscountException;
import com.liftoff.project.exception.product.ProductNotEnoughQuantityException;
import com.liftoff.project.exception.product.ProductNotFoundException;
import com.liftoff.project.exception.product.ProductOutOfStockException;
import com.liftoff.project.exception.storage.FileNotFoundException;
import com.liftoff.project.exception.storage.FileSizeExceedsLimitException;
import com.liftoff.project.exception.storage.ImageNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFoundException(ProductNotFoundException ex) {
        return createErrorResponse(ex.getMessage(), ex.getStatus());
    }

//    @ExceptionHandler(CategoryNotFoundException.class)
//    public ResponseEntity<Map<String, String>> handleCategoryNotFoundException(CategoryNotFoundException ex) {
//        return createErrorResponse(ex.getMessage(), ex.getStatus());
//    }

//    @ExceptionHandler(ParentCategoryNotFoundException.class)
//    public ResponseEntity<Map<String, String>> handleParentCategoryNotFoundException(ParentCategoryNotFoundException ex) {
//        return createErrorResponse(ex.getMessage(), ex.getStatus());
//    }

    @ExceptionHandler(CannotDeleteCategoryException.class)
    public ResponseEntity<Map<String, String>> handleCannotDeleteCategoryException(CannotDeleteCategoryException ex) {
        return createErrorResponse(ex.getMessage(), ex.getStatus());
    }

    @ExceptionHandler(InvalidParentCategoryException.class)
    public ResponseEntity<Map<String, String>> handleInvalidParentCategoryException(InvalidParentCategoryException ex) {
        return createErrorResponse(ex.getMessage(), ex.getStatus());

    }

    @ExceptionHandler(UsernameNotFoundException.class) // Security
    public ResponseEntity<Map<String, String>> handleUserSecurityDetailsNotFoundException(UsernameNotFoundException ex) {
        return createErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(FileSizeExceedsLimitException.class)
    public ResponseEntity<Map<String, String>> handleFileSizeExceedsLimitException(FileSizeExceedsLimitException ex) {
        return createErrorResponse(ex.getMessage(), ex.getStatus());
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleFileNotFoundException(FileNotFoundException ex) {
        return createErrorResponse(ex.getMessage(), ex.getStatus());
    }

    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleImageNotFoundException(ImageNotFoundException ex) {
        return createErrorResponse(ex.getMessage(), ex.getStatus());
    }

//    @ExceptionHandler(LoginAuthenticationException.class)
//    public ResponseEntity<Map<String, String>> handleInvalidLoginException(LoginAuthenticationException ex) {
//        return createErrorResponse(ex.getMessage(), ex.getStatus());
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleInvalidLoginFieldsException(MethodArgumentNotValidException ex) {

        return createErrorResponse(ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CookieNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCookiesNotFoundException(CookieNotFoundException ex) {

        return createErrorResponse(ex.getMessage(), ex.getStatus());
    }

//    @ExceptionHandler(CartNotFoundException.class)
//    public ResponseEntity<Map<String, String>> handleCartNotFoundException(CartNotFoundException ex) {
//        return createErrorResponse(ex.getMessage(), ex.getStatus());
//    }

    @ExceptionHandler(ProductOutOfStockException.class)
    public ResponseEntity<Map<String, String>> handleProductOutOfStockException(ProductOutOfStockException ex) {
        return createErrorResponse(ex.getMessage(), ex.getStatus());
    }

//    @ExceptionHandler(UserNotFoundException.class)
//    public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException ex) {
//        return createErrorResponse(ex.getMessage(), ex.getStatus());
//    }

    @ExceptionHandler(ProductNotEnoughQuantityException.class)
    public ResponseEntity<Map<String, String>> handleProductNotEnoughQuantityException
            (ProductNotEnoughQuantityException ex) {
        return createErrorResponse(ex.getMessage(), ex.getStatus());
    }

    @ExceptionHandler(InvalidDiscountException.class)
    public ResponseEntity<Map<String, String>> handleInvalidDiscountException
            (InvalidDiscountException ex) {
        return createErrorResponse(ex.getMessage(), ex.getStatus());
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFoundException(EntityNotFoundException ex) {
        return createErrorResponse(ex.getMessage(), ex.getStatus());
    }

    @ExceptionHandler(TermsNotAcceptedException.class)
    public ResponseEntity<Map<String, String>> handleTermsNotAcceptedException(TermsNotAcceptedException ex) {
        return createErrorResponse(ex.getMessage(), ex.getStatus());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, String>> handleBusinessException(BusinessException ex) {
        return createErrorResponse(ex.getMessage(), ex.getStatus());
    }

    @ExceptionHandler(TechnicalException.class)
    public ResponseEntity<Map<String, String>> handleTechnicalException(TechnicalException ex) {
        return createErrorResponse(ex.getMessage(), ex.getStatus());
    }



    private ResponseEntity<Map<String, String>> createErrorResponse(String errorMessage, HttpStatus status) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", errorMessage);
        return ResponseEntity.status(status).body(errorResponse);
    }

}
