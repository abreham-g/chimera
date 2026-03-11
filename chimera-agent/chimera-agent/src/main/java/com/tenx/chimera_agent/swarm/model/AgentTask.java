package com.tenx.chimera_agent.swarm.model;

// File purpose: Defines AgentTask behavior for Project Chimera.

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record AgentTask(
		String taskId,
		TaskType taskType,
		TaskPriority priority,
		TaskContext context,
		String assignedWorkerId,
		Instant createdAt,
		TaskStatus status,
		long stateVersion
) {
	public AgentTask {
		Objects.requireNonNull(taskId, "taskId");
		Objects.requireNonNull(taskType, "taskType");
		Objects.requireNonNull(priority, "priority");
		Objects.requireNonNull(context, "context");
		Objects.requireNonNull(assignedWorkerId, "assignedWorkerId");
		Objects.requireNonNull(createdAt, "createdAt");
		Objects.requireNonNull(status, "status");
	}

	public static AgentTask createGoalTask(String goalDescription, long stateVersion) {
		return new AgentTask(
				UUID.randomUUID().toString(),
				TaskType.GENERATE_CONTENT,
				TaskPriority.HIGH,
				new TaskContext(goalDescription, java.util.List.of(), java.util.List.of()),
				"worker-pool",
				Instant.now(),
				TaskStatus.PENDING,
				stateVersion
		);
	}
}

