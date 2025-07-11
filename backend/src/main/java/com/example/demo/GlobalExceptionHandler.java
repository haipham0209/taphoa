package com.example.demo;

import com.example.demo.dto.ErrorResponseDto;
import com.example.demo.exception.DeletedUserException;
import com.example.demo.exception.PendingUserException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// Login
	@ExceptionHandler(InternalAuthenticationServiceException.class)
	public ResponseEntity<ErrorResponseDto> handleInternalAuthException(InternalAuthenticationServiceException ex) {
		Throwable cause = ex.getCause();

		if (cause instanceof DeletedUserException) {
			return forbidden("Account is deleted");
		} else if (cause instanceof PendingUserException) {
			return forbidden("Account is on pending");
		} else if (cause instanceof DisabledException) {
			return forbidden("Account is not activated");
		}

		return unauthorized("System Error Unknow Error 1001");
	}

	// Wrong Login info
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorResponseDto> handleBadCredentials(BadCredentialsException ex) {
		return unauthorized("Email or password invalid");
	}

	// Register Validate
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}

		String errorMessage = errors.entrySet().stream().map(e -> e.getValue())
				.reduce((m1, m2) -> m1 + "; " + m2).orElse("Validation error");

		return badRequest(errorMessage);
	}

	// insert dulpicate
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponseDto> handleDuplicate(DataIntegrityViolationException ex) {
	    String message = ex.getMessage();

	    if (message.contains("users.email")) {
	        return conflict("Email is existed");
	    } else if (message.contains("users.user_name")) {
	        return conflict("Username is existed");
	    }

	    return conflict("System Error 1004");
	}
	
	private ResponseEntity<ErrorResponseDto> conflict(String message) {
	    ErrorResponseDto response = new ErrorResponseDto();
	    response.setMessage(message);
	    response.setStatus(409);  
	    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}

	//
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponseDto> handleIllegalArgument(IllegalArgumentException ex) {
		return badRequest(ex.getMessage());
	}

	//
	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<ErrorResponseDto> handleIllegalState(IllegalStateException ex) {
		return unauthorized(ex.getMessage());
	}

	//
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponseDto> handleRuntimeException(RuntimeException ex) {
		return badRequest("System Error 1003 " + ex.getMessage());
	}

	//
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponseDto> handleException(Exception ex) {
		ex.printStackTrace();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErrorResponseDto("Unknow Error 1002", 500));
	}

	// ===================== HELPERS ======================

	private ResponseEntity<ErrorResponseDto> badRequest(String message) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponseDto(message, HttpStatus.BAD_REQUEST.value()));
	}

	private ResponseEntity<ErrorResponseDto> unauthorized(String message) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new ErrorResponseDto(message, HttpStatus.UNAUTHORIZED.value()));
	}

	private ResponseEntity<ErrorResponseDto> forbidden(String message) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(new ErrorResponseDto(message, HttpStatus.FORBIDDEN.value()));
	}
	
}
