package com.tenx.chimera_agent.api.contract;

// File purpose: Defines RerunResponse behavior for Project Chimera.

import com.fasterxml.jackson.annotation.JsonProperty;

public record RerunResponse(
		@JsonProperty("source_run_id")
		String sourceRunId,
		@JsonProperty("new_run_id")
		String newRunId,
		@JsonProperty("status")
		String status
) {
}

