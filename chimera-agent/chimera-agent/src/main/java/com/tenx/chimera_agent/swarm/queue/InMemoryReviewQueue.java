package com.tenx.chimera_agent.swarm.queue;

// File purpose: Defines InMemoryReviewQueue behavior for Project Chimera.

import com.tenx.chimera_agent.swarm.model.WorkerResult;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class InMemoryReviewQueue implements ReviewQueue {
	private final Queue<WorkerResult> queue = new ConcurrentLinkedQueue<>();

	@Override
	public void push(WorkerResult result) {
		queue.add(result);
	}

	@Override
	public Optional<WorkerResult> poll() {
		return Optional.ofNullable(queue.poll());
	}

	@Override
	public int size() {
		return queue.size();
	}
}

