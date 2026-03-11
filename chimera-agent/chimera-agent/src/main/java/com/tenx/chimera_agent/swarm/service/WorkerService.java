package com.tenx.chimera_agent.swarm.service;

// File purpose: Defines WorkerService behavior for Project Chimera.

import com.tenx.chimera_agent.swarm.model.AgentTask;
import com.tenx.chimera_agent.swarm.model.TaskPriority;
import com.tenx.chimera_agent.swarm.model.WorkerResult;
import com.tenx.chimera_agent.swarm.queue.ReviewQueue;
import com.tenx.chimera_agent.swarm.queue.TaskQueue;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class WorkerService {
	private final TaskQueue taskQueue;
	private final ReviewQueue reviewQueue;

	public WorkerService(TaskQueue taskQueue, ReviewQueue reviewQueue) {
		this.taskQueue = taskQueue;
		this.reviewQueue = reviewQueue;
	}

	public Optional<WorkerResult> processNextTask(String workerId) {
		Optional<AgentTask> task = taskQueue.poll();
		if (task.isEmpty()) {
			return Optional.empty();
		}

		AgentTask nextTask = task.get();
		double confidenceScore = calculateConfidence(nextTask.priority());
		WorkerResult result = new WorkerResult(
				nextTask.taskId(),
				workerId,
				"Processed task for goal: " + nextTask.context().goalDescription(),
				confidenceScore,
				nextTask.stateVersion(),
				Instant.now()
		);
		reviewQueue.push(result);
		return Optional.of(result);
	}

	private double calculateConfidence(TaskPriority priority) {
		return switch (priority) {
			case HIGH -> 0.95;
			case MEDIUM -> 0.82;
			case LOW -> 0.65;
		};
	}
}

