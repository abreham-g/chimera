package com.tenx.chimera_agent.swarm.service;

// File purpose: Defines JudgeService behavior for Project Chimera.

import com.tenx.chimera_agent.swarm.model.GlobalState;
import com.tenx.chimera_agent.swarm.model.JudgeDecision;
import com.tenx.chimera_agent.swarm.model.ReviewOutcome;
import com.tenx.chimera_agent.swarm.model.WorkerResult;
import com.tenx.chimera_agent.swarm.queue.ReviewQueue;
import com.tenx.chimera_agent.swarm.state.GlobalStateRepository;
import com.tenx.chimera_agent.swarm.state.OccConflictException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JudgeService {
	private final ReviewQueue reviewQueue;
	private final GlobalStateRepository globalStateRepository;

	public JudgeService(ReviewQueue reviewQueue, GlobalStateRepository globalStateRepository) {
		this.reviewQueue = reviewQueue;
		this.globalStateRepository = globalStateRepository;
	}

	public Optional<JudgeDecision> reviewNextResult() {
		Optional<WorkerResult> pending = reviewQueue.poll();
		if (pending.isEmpty()) {
			return Optional.empty();
		}

		WorkerResult result = pending.get();
		GlobalState currentState = globalStateRepository.getCurrentState();

		if (result.confidenceScore() < 0.70) {
			return Optional.of(new JudgeDecision(
					result.taskId(),
					ReviewOutcome.REJECTED_RETRY,
					"Rejected due to low confidence score.",
					currentState.version()
			));
		}

		if (result.confidenceScore() < 0.90) {
			return Optional.of(new JudgeDecision(
					result.taskId(),
					ReviewOutcome.ESCALATED_HITL,
					"Escalated to human review due to medium confidence score.",
					currentState.version()
			));
		}

		if (currentState.version() != result.expectedStateVersion()) {
			throw new OccConflictException("State version mismatch detected before commit.");
		}

		GlobalState committedState = currentState.addCompletedTask(result.taskId());
		boolean updated = globalStateRepository.compareAndSet(currentState.version(), committedState);
		if (!updated) {
			throw new OccConflictException("State changed concurrently while committing judge decision.");
		}

		return Optional.of(new JudgeDecision(
				result.taskId(),
				ReviewOutcome.APPROVED,
				"Approved and committed to global state.",
				committedState.version()
		));
	}
}

