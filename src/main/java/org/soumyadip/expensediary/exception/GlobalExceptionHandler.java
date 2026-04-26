package org.soumyadip.expensediary.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        log.warn("Invalid refresh token attempt at ip={} path={}",
                request.getRemoteAddr(),
                request.getRequestURI()
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(
            Exception e,
            HttpServletRequest request
    ){
        log.error("Uncaught exception in request handler path={}",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error!");
    }
}
