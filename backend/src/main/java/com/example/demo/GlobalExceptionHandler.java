package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.dto.ErrorResponseDto;
import com.example.demo.exception.DeletedUserException;
import com.example.demo.exception.PendingUserException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(InternalAuthenticationServiceException.class)
	public ResponseEntity<ErrorResponseDto> handleInternalAuthException(InternalAuthenticationServiceException ex) {
		Throwable cause = ex.getCause();

		if (cause instanceof DeletedUserException) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(new ErrorResponseDto("Account is deleted", HttpStatus.FORBIDDEN.value()));
		} else if (cause instanceof PendingUserException) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(new ErrorResponseDto("Account is on pending", HttpStatus.FORBIDDEN.value()));
		} else if (cause instanceof DisabledException) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(new ErrorResponseDto("Account is not activated", HttpStatus.FORBIDDEN.value()));
		}

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new ErrorResponseDto("System Error Unknow Error 1001", HttpStatus.UNAUTHORIZED.value()));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponseDto> handleIllegalArgument(IllegalArgumentException ex) {
		ErrorResponseDto error = new ErrorResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorResponseDto> handleBadCredentials(BadCredentialsException ex) {
		ErrorResponseDto error = new ErrorResponseDto("email or password not true", HttpStatus.UNAUTHORIZED.value());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponseDto> handleRuntimeException(RuntimeException ex) {
		ErrorResponseDto error = new ErrorResponseDto("System Error 1003 " + ex.getMessage(),
				HttpStatus.BAD_REQUEST.value());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponseDto> handleException(Exception ex) {
		ex.printStackTrace();
		ErrorResponseDto error = new ErrorResponseDto("System Error Unknow Error 1002",
				HttpStatus.INTERNAL_SERVER_ERROR.value());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}
}
