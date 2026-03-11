package com.tenx.chimera_agent.swarm.model;

// File purpose: Defines JudgeDecision behavior for Project Chimera.

import java.util.Objects;

public record JudgeDecision(
		String taskId,
		ReviewOutcome outcome,
		String reason,
		long resultingStateVersion
) {
	public JudgeDecision {
		Objects.requireNonNull(taskId, "taskId");
		Objects.requireNonNull(outcome, "outcome");
		Objects.requireNonNull(reason, "reason");
	}
}

