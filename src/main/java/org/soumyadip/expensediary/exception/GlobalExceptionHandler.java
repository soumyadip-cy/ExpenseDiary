package org.soumyadip.expensediary.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(
            Exception e,
            HttpServletRequest request
    ){
        log.error("Uncaught exception in request handler error={} path={}",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error!");
    }

}
