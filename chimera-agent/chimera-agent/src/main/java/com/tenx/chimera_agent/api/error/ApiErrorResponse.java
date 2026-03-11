package com.tenx.chimera_agent.api.error;

// File purpose: Defines ApiErrorResponse behavior for Project Chimera.

import com.fasterxml.jackson.annotation.JsonProperty;

public record ApiErrorResponse(
		@JsonProperty("error")
		ApiError error
) {
}

