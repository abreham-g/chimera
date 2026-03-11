package com.tenx.chimera_agent.api.contract;

// File purpose: Defines TrendItem behavior for Project Chimera.

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public record TrendItem(
		@JsonProperty("video_id")
		String videoId,
		@JsonProperty("title")
		String title,
		@JsonProperty("creator_handle")
		String creatorHandle,
		@JsonProperty("trend_score")
		double trendScore,
		@JsonProperty("views")
		long views,
		@JsonProperty("captured_at")
		Instant capturedAt
) {
}

