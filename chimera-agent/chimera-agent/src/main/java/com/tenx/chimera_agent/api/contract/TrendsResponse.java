package com.tenx.chimera_agent.api.contract;

import java.util.List;

public record TrendsResponse(
		int windowHours,
		String platform,
		List<TrendItem> items
) {
}

