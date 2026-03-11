package com.tenx.chimera_agent.run;

// File purpose: Defines RunRegistryService behavior for Project Chimera.

import com.tenx.chimera_agent.api.contract.CollectRunRequest;
import com.tenx.chimera_agent.api.error.ApiErrorCode;
import com.tenx.chimera_agent.api.error.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RunRegistryService {
	// Challenge-phase storage: in-memory run registry; swap with persistent storage in Day 3+.
	private final Map<String, RunRecord> runs = new ConcurrentHashMap<>();

	public String registerCollectRun(CollectRunRequest request) {
		Objects.requireNonNull(request, "request");
		String runId = nextRunId();
		runs.put(
				runId,
				new RunRecord(
						runId,
						"collect",
						null,
						Instant.now(),
						Map.of(
								"platforms", request.platforms(),
								"window_minutes", request.windowMinutes(),
								"limit_per_platform", request.limitPerPlatform()
						)
				)
		);
		return runId;
	}

	public String registerRerun(String sourceRunId, String reason) {
		Objects.requireNonNull(sourceRunId, "sourceRunId");
		Objects.requireNonNull(reason, "reason");
		RunRecord source = runs.get(sourceRunId);
		if (source == null) {
			throw new ApiException(
					HttpStatus.NOT_FOUND,
					ApiErrorCode.VALIDATION_ERROR,
					"Unknown source run_id: " + sourceRunId,
					false,
					Map.of("field", "run_id", "value", sourceRunId)
			);
		}

		String runId = nextRunId();
		runs.put(
				runId,
				new RunRecord(
						runId,
						"rerun",
						source.runId(),
						Instant.now(),
						Map.of("reason", reason)
				)
		);
		return runId;
	}

	private String nextRunId() {
		// Keep run IDs human-readable and grep-friendly in logs.
		return "run_" + UUID.randomUUID();
	}

	private record RunRecord(
			String runId,
			String workflowType,
			String sourceRunId,
			Instant queuedAt,
			Map<String, Object> metadata
	) {
	}
}

