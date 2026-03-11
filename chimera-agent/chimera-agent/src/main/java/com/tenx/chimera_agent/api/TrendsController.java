package com.tenx.chimera_agent.api;

import com.tenx.chimera_agent.api.contract.TrendsResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trends")
public class TrendsController {

	@GetMapping
	public TrendsResponse getTrends(
			@RequestParam(name = "platform", required = false) String platform,
			@RequestParam(name = "window_hours", defaultValue = "24") int windowHours,
			@RequestParam(name = "limit", defaultValue = "20") int limit
	) {
		// Minimal contract-first endpoint: returns an empty list until persistence/scoring are implemented.
		// Validation rules (e.g., limit max 200) will be added alongside the real query implementation.
		return new TrendsResponse(windowHours, platform, List.of());
	}
}

