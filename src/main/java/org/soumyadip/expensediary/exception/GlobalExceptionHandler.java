package org.soumyadip.expensediary.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.soumyadip.expensediary.dto.ApiMessage;
import org.soumyadip.expensediary.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<String> handleInvalidRefreshTokenException(
            InvalidRefreshTokenException e,
            HttpServletRequest request
    ){
        log.warn("Invalid refresh token attempt at ip={} path={} reason={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                e.getMessage()
                );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token!");
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<String> handleRefreshTokenExpiredException(
            RefreshTokenExpiredException e,
            HttpServletRequest request
    ){
        log.info("Expired refresh token ip={} path={}",
                request.getRemoteAddr(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token expired!");
    }

    @ExceptionHandler(UsernameAlreadyExists.class)
    public ResponseEntity<String> handleUsernameAlreadyExists(
            UserCreationException e,
            HttpServletRequest request
    ) {
        log.error("Duplicate username ! | Error message: "+ e.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body("Username: " + e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(
            UserNotFoundException e,
            HttpServletRequest request
    ) {
        log.error("User not found ! | Error message: "+ e.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body("Username: " + e.getMessage());
    }

    @ExceptionHandler(UserCreationException.class)
    public ResponseEntity<String> handleUserCreationException(
            UserCreationException e,
            HttpServletRequest request
    ) {
        log.error("User creation failed! | Error message: "+ e.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body("User creation failed!");
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<String> handlePasswordMismatchException() {
        log.error("Password mismatch!");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password mismatch!");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(
            IllegalArgumentException exception
    ) {
        log.error("Illegal argument! | Error message: "+exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Illegal argument!");
    }

    @ExceptionHandler(UserDeleteException.class)
    public ResponseEntity<String> handleUserDeleteException(
            UserDeleteException e
    ) {
        log.error("User delete failed! | Error message: "+e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User delete failed!");
    }

    @ExceptionHandler(JwtExpireTokenCreationException.class)
    public ResponseEntity<String> handleJwtExpireTokenCreationException(
            JwtExpireTokenCreationException e
    ) {
        log.error("Exception creating entry for JWT claim ID during logout  | Error message: "+ e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("JWT claim ID during logout error!");
    }

    @ExceptionHandler(TransactionTypeNotFoundException.class)
    public ResponseEntity<ApiResponse<ApiMessage>> handleTransactionTypeNotFoundException(
            TransactionTypeNotFoundException e
    ) {
        log.error("TransactionType with {} not found! | Error message: {}", e.getId(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiResponse<>(
                        false,
                    new ApiMessage("Transaction Type not found!"),
                    HttpStatus.NOT_FOUND.value(),
                    Instant.now()
                )
        );
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ApiResponse<ApiMessage>> handleTransactionNotFoundException(
            TransactionNotFoundException e
    ) {
        log.error("Transaction with id: {} not found! | Error message: {}",e.getId(),e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiResponse<>(
                        false,
                        new ApiMessage("Transaction not found!"),
                        HttpStatus.NOT_FOUND.value(),
                        Instant.now()
                )
        );
    }

    @ExceptionHandler(MerchantNotFoundException.class)
    public ResponseEntity<ApiResponse<ApiMessage>> handleMerchantNotFoundException(
            MerchantNotFoundException e
    ) {
        log.error("Merchant with id: {} not found! | Error message: {}",e.getId(),e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiResponse<>(
                        false,
                        new ApiMessage("Merchant not found!"),
                        HttpStatus.NOT_FOUND.value(),
                        Instant.now()
                )
        );
    }

    @ExceptionHandler(BeneficiaryNotFoundException.class)
    public ResponseEntity<ApiResponse<ApiMessage>> handleBeneficiaryNotFoundException(
            BeneficiaryNotFoundException e
    ) {
        log.error("Beneficiary with id: {} not found! | Error message: {}",e.getId(),e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiResponse<>(
                        false,
                        new ApiMessage("Beneficiary not found!"),
                        HttpStatus.NOT_FOUND.value(),
                        Instant.now()
                )
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<LinkedHashMap<String, LinkedList<String>>>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {


        LinkedHashMap<String, LinkedList<String>> errors = e.getBindingResult().
                getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        LinkedHashMap::new,
                        Collectors.mapping(
                                FieldError::getDefaultMessage,
                                Collectors.toCollection(LinkedList::new)
                        )
                ));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiResponse<>(
                        false,
                        errors,
                        HttpStatus.BAD_REQUEST.value(),
                        Instant.now()
                )
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<ApiMessage>> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e,
            HttpServletRequest request
    ) {
        String supportedMethods = e.getSupportedMethods() == null ? "null" : Arrays.toString(e.getSupportedMethods());
        log.error(
                "Method not supported! | Error message: {} | Method: {} | Path: {} | SupportedMethods: {}",
                e.getMessage(),
                request.getMethod(),
                request.getRequestURI(),
                supportedMethods);

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ApiResponse<>(
                false,
                new ApiMessage(
                        String.format("HTTP method '%s' is not supported for '%s'",
                                request.getMethod(),
                                request.getRequestURI())
                ),
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                Instant.now()
        ));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<ApiMessage>> handleAccessDeniedException(
            AccessDeniedException e,
            HttpServletRequest request
    ) {
        log.error("User tried accessing endpoint without sufficient privileges ! | Error message: {}",e.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ApiResponse<>(
                        false,
                        new ApiMessage("This user is not permitted to perform this operation!"),
                        HttpStatus.FORBIDDEN.value(),
                        Instant.now()
                )
        );
    }

    @ExceptionHandler(MerchantAlreadyExists.class)
    public ResponseEntity<ApiResponse<ApiMessage>> handleMerchantAlreadyExists(
            MerchantAlreadyExists e,
            HttpServletRequest request
    ) {
        log.error("User tried creating duplicate merchant ! | Error message: {}",e.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ApiResponse<>(
                        false,
                        new ApiMessage("Merchant already exists!"),
                        HttpStatus.FORBIDDEN.value(),
                        Instant.now()
                )
        );
    }

    @ExceptionHandler(BeneficiaryAlreadyExists.class)
    public ResponseEntity<ApiResponse<ApiMessage>> handleBeneficiaryAlreadyExists(
            BeneficiaryAlreadyExists e,
            HttpServletRequest request
    ) {
        log.error("User tried creating duplicate beneficiary ! | Error message: {}",e.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ApiResponse<>(
                        false,
                        new ApiMessage("Beneficiary already exists!"),
                        HttpStatus.FORBIDDEN.value(),
                        Instant.now()
                )
        );
    }

    @ExceptionHandler(TransactionTypeAlreadyExists.class)
    public ResponseEntity<ApiResponse<ApiMessage>> handleTransactionTypeAlreadyExists(
            TransactionTypeAlreadyExists e,
            HttpServletRequest request
    ) {
        log.error("User tried creating duplicate transaction type ! | Error message: {}",e.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ApiResponse<>(
                        false,
                        new ApiMessage("Transaction type already exists!"),
                        HttpStatus.FORBIDDEN.value(),
                        Instant.now()
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ApiMessage>> handleException(
            Exception e,
            HttpServletRequest request
    ){
        log.error("Uncaught exception in request handler error={} path={}",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new  ApiResponse<>(
                        false,
                        new ApiMessage("Uncaught exception encountered! Please contact admin!"),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        Instant.now()
                )
        );
    }

}
