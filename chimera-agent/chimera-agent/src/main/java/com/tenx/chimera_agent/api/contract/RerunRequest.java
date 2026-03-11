package com.tenx.chimera_agent.api.contract;

// File purpose: Defines RerunRequest behavior for Project Chimera.

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record RerunRequest(
		@JsonProperty("reason")
		@NotBlank
		String reason
) {
}

