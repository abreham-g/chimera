package com.tenx.chimera_agent.api.error;

// File purpose: Defines ApiException behavior for Project Chimera.

import org.springframework.http.HttpStatus;

import java.util.Map;

public class ApiException extends RuntimeException {
	private final HttpStatus status;
	private final ApiErrorCode code;
	private final boolean retryable;
	private final Map<String, Object> details;

	public ApiException(HttpStatus status, ApiErrorCode code, String message, boolean retryable, Map<String, Object> details) {
		super(message);
		this.status = status;
		this.code = code;
		this.retryable = retryable;
		this.details = details;
	}

	public HttpStatus status() {
		return status;
	}

	public ApiErrorCode code() {
		return code;
	}

	public boolean retryable() {
		return retryable;
	}

	public Map<String, Object> details() {
		return details;
	}
}

