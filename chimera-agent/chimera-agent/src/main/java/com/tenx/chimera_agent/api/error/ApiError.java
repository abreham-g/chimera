package com.tenx.chimera_agent.api.error;

// File purpose: Defines ApiError behavior for Project Chimera.

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record ApiError(
		@JsonProperty("code")
		String code,
		@JsonProperty("message")
		String message,
		@JsonProperty("retryable")
		boolean retryable,
		@JsonProperty("details")
		Map<String, Object> details,
		@JsonProperty("trace_id")
		String traceId
) {
}

