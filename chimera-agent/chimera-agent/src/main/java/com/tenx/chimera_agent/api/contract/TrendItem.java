package com.tenx.chimera_agent.api.contract;

import java.time.Instant;

public record TrendItem(
		String videoId,
		String title,
		String creatorHandle,
		double trendScore,
		long views,
		Instant capturedAt
) {
}

