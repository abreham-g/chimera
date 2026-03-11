package com.tenx.chimera_agent.api;

// File purpose: Defines TrendsController behavior for Project Chimera.

import com.tenx.chimera_agent.api.contract.TrendsResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/v1/trends")
public class TrendsController {

	@GetMapping
	public TrendsResponse getTrends(
			@RequestParam(name = "platform", required = false) String platform,
			@RequestParam(name = "window_hours", defaultValue = "24") @Min(1) @Max(168) int windowHours,
			@RequestParam(name = "limit", defaultValue = "20") @Min(1) @Max(200) int limit
	) {
		// Contract-first endpoint: query and persistence wiring are added in later phases.
		return new TrendsResponse(windowHours, platform, List.of());
	}
}

