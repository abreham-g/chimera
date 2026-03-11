package com.tenx.chimera_agent.swarm.model;

// File purpose: Defines WorkerResult behavior for Project Chimera.

import java.time.Instant;
import java.util.Objects;

public record WorkerResult(
		String taskId,
		String workerId,
		String output,
		double confidenceScore,
		long expectedStateVersion,
		Instant completedAt
) {
	public WorkerResult {
		Objects.requireNonNull(taskId, "taskId");
		Objects.requireNonNull(workerId, "workerId");
		Objects.requireNonNull(output, "output");
		Objects.requireNonNull(completedAt, "completedAt");
	}
}

