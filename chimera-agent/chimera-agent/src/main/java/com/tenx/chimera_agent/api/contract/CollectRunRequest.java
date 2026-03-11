package com.tenx.chimera_agent.api.contract;

// File purpose: Defines CollectRunRequest behavior for Project Chimera.

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Objects;

public record CollectRunRequest(
		@JsonProperty("platforms")
		@NotEmpty
		List<@NotBlank String> platforms,
		@JsonProperty("window_minutes")
		@Min(1)
		@Max(240)
		int windowMinutes,
		@JsonProperty("limit_per_platform")
		@Min(1)
		@Max(200)
		int limitPerPlatform
) {
	public CollectRunRequest {
		platforms = List.copyOf(Objects.requireNonNull(platforms, "platforms"));
	}
}

