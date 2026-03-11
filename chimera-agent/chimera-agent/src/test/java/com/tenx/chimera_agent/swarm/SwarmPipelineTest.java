package com.tenx.chimera_agent.swarm;

// File purpose: Defines SwarmPipelineTest behavior for Project Chimera.

import com.tenx.chimera_agent.swarm.model.AgentTask;
import com.tenx.chimera_agent.swarm.model.JudgeDecision;
import com.tenx.chimera_agent.swarm.model.ReviewOutcome;
import com.tenx.chimera_agent.swarm.model.WorkerResult;
import com.tenx.chimera_agent.swarm.queue.InMemoryReviewQueue;
import com.tenx.chimera_agent.swarm.queue.InMemoryTaskQueue;
import com.tenx.chimera_agent.swarm.service.JudgeService;
import com.tenx.chimera_agent.swarm.service.PlannerService;
import com.tenx.chimera_agent.swarm.service.WorkerService;
import com.tenx.chimera_agent.swarm.state.InMemoryGlobalStateRepository;
import com.tenx.chimera_agent.swarm.state.OccConflictException;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SwarmPipelineTest {

	@Test
	void plannerWorkerJudgePipelineApprovesHighConfidenceTasks() {
		InMemoryTaskQueue taskQueue = new InMemoryTaskQueue();
		InMemoryReviewQueue reviewQueue = new InMemoryReviewQueue();
		InMemoryGlobalStateRepository stateRepository = new InMemoryGlobalStateRepository();

		PlannerService planner = new PlannerService(taskQueue, stateRepository);
		WorkerService worker = new WorkerService(taskQueue, reviewQueue);
		JudgeService judge = new JudgeService(reviewQueue, stateRepository);

		List<AgentTask> planned = planner.planGoals(List.of("Collect trends from youtube_shorts"));
		assertEquals(1, planned.size());
		assertEquals(1, taskQueue.size());

		var workerResult = worker.processNextTask("worker-1");
		assertTrue(workerResult.isPresent());
		assertEquals(0, taskQueue.size());
		assertEquals(1, reviewQueue.size());

		var decision = judge.reviewNextResult();
		assertTrue(decision.isPresent());
		assertEquals(ReviewOutcome.APPROVED, decision.get().outcome());
		assertEquals(1L, decision.get().resultingStateVersion());
		assertEquals(1, stateRepository.getCurrentState().completedTaskIds().size());
	}

	@Test
	void judgeDetectsOccConflictWhenStateVersionDrifts() {
		InMemoryReviewQueue reviewQueue = new InMemoryReviewQueue();
		InMemoryGlobalStateRepository stateRepository = new InMemoryGlobalStateRepository();
		JudgeService judge = new JudgeService(reviewQueue, stateRepository);

		reviewQueue.push(new WorkerResult(
				"task-1",
				"worker-1",
				"output",
				0.95,
				0L,
				Instant.now()
		));

		boolean stateUpdated = stateRepository.compareAndSet(
				0L,
				stateRepository.getCurrentState().addCompletedTask("other-task")
		);
		assertTrue(stateUpdated);

		assertThrows(OccConflictException.class, judge::reviewNextResult);
		assertFalse(stateRepository.getCurrentState().completedTaskIds().contains("task-1"));
	}

	@Test
	void judgeEscalatesMediumConfidenceToHitl() {
		InMemoryReviewQueue reviewQueue = new InMemoryReviewQueue();
		InMemoryGlobalStateRepository stateRepository = new InMemoryGlobalStateRepository();
		JudgeService judge = new JudgeService(reviewQueue, stateRepository);

		reviewQueue.push(new WorkerResult(
				"task-2",
				"worker-2",
				"output",
				0.82,
				0L,
				Instant.now()
		));

		JudgeDecision decision = judge.reviewNextResult().orElseThrow();
		assertEquals(ReviewOutcome.ESCALATED_HITL, decision.outcome());
		assertEquals(0L, stateRepository.getCurrentState().version());
	}
}

