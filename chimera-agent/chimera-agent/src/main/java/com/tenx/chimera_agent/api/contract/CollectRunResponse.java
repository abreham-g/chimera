package com.tenx.chimera_agent.api.contract;

// File purpose: Defines CollectRunResponse behavior for Project Chimera.

import com.fasterxml.jackson.annotation.JsonProperty;

public record CollectRunResponse(
		@JsonProperty("run_id")
		String runId,
		@JsonProperty("status")
		String status
) {
}

