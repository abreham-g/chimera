package com.tenx.chimera_agent.swarm.model;

// File purpose: Defines GlobalState behavior for Project Chimera.

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record GlobalState(
		long version,
		List<String> activeGoals,
		List<String> completedTaskIds,
		Instant updatedAt
) {
	public GlobalState {
		activeGoals = List.copyOf(Objects.requireNonNull(activeGoals, "activeGoals"));
		completedTaskIds = List.copyOf(Objects.requireNonNull(completedTaskIds, "completedTaskIds"));
		Objects.requireNonNull(updatedAt, "updatedAt");
	}

	public static GlobalState initial() {
		return new GlobalState(0L, List.of(), List.of(), Instant.now());
	}

	public GlobalState withActiveGoals(List<String> goals) {
		return new GlobalState(version + 1, goals, completedTaskIds, Instant.now());
	}

	public GlobalState addCompletedTask(String taskId) {
		List<String> newCompleted = new ArrayList<>(completedTaskIds);
		newCompleted.add(taskId);
		return new GlobalState(version + 1, activeGoals, newCompleted, Instant.now());
	}
}

