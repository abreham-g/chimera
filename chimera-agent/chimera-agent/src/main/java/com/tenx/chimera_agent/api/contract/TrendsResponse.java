package com.tenx.chimera_agent.api.contract;

// File purpose: Defines TrendsResponse behavior for Project Chimera.

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record TrendsResponse(
		@JsonProperty("window_hours")
		int windowHours,
		@JsonProperty("platform")
		String platform,
		@JsonProperty("items")
		List<TrendItem> items
) {
}

