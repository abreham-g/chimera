package com.tenx.chimera_agent.api.error;

// File purpose: Defines GlobalExceptionHandler behavior for Project Chimera.

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ApiErrorResponse> handleApiException(ApiException exception) {
		return buildResponse(
				exception.status(),
				exception.code(),
				exception.getMessage(),
				exception.retryable(),
				exception.details()
		);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
		// Return the first validation violation to keep the payload deterministic for clients/tests.
		FieldError firstError = exception.getBindingResult().getFieldErrors().stream()
				.findFirst()
				.orElse(null);

		Map<String, Object> details = firstError == null
				? Map.of("field", "unknown", "value", "unknown")
				: Map.of("field", firstError.getField(), "value", String.valueOf(firstError.getRejectedValue()));

		String message = firstError == null
				? "Request validation failed"
				: firstError.getDefaultMessage();

		return buildResponse(
				HttpStatus.BAD_REQUEST,
				ApiErrorCode.VALIDATION_ERROR,
				message,
				false,
				details
		);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException exception) {
		var first = exception.getConstraintViolations().stream().findFirst().orElse(null);
		Map<String, Object> details = first == null
				? Map.of("field", "unknown", "value", "unknown")
				: Map.of("field", first.getPropertyPath().toString(), "value", String.valueOf(first.getInvalidValue()));

		String message = first == null ? "Constraint validation failed" : first.getMessage();

		return buildResponse(
				HttpStatus.BAD_REQUEST,
				ApiErrorCode.VALIDATION_ERROR,
				message,
				false,
				details
		);
	}

	@ExceptionHandler({HttpMessageNotReadableException.class, IllegalArgumentException.class})
	public ResponseEntity<ApiErrorResponse> handleBadRequest(Exception exception) {
		return buildResponse(
				HttpStatus.BAD_REQUEST,
				ApiErrorCode.VALIDATION_ERROR,
				safeMessage(exception, "Invalid request payload"),
				false,
				Map.of()
		);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorResponse> handleUnhandled(Exception exception) {
		return buildResponse(
				HttpStatus.INTERNAL_SERVER_ERROR,
				ApiErrorCode.INTERNAL_ERROR,
				"Unexpected server error",
				false,
				Map.of("exception", exception.getClass().getSimpleName())
		);
	}

	private ResponseEntity<ApiErrorResponse> buildResponse(
			HttpStatus status,
			ApiErrorCode code,
			String message,
			boolean retryable,
			Map<String, Object> details
	) {
		ApiError error = new ApiError(
				code.name(),
				message,
				retryable,
				details == null ? Map.of() : details,
				generateTraceId()
		);
		return ResponseEntity.status(status).body(new ApiErrorResponse(error));
	}

	private String generateTraceId() {
		// Short trace ID that still lets us correlate API errors across logs.
		return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
	}

	private String safeMessage(Exception exception, String fallback) {
		String message = exception.getMessage();
		return message == null || message.isBlank() ? fallback : message;
	}
}

