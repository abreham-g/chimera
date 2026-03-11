package com.tenx.chimera_agent.swarm.queue;

// File purpose: Defines InMemoryTaskQueue behavior for Project Chimera.

import com.tenx.chimera_agent.swarm.model.AgentTask;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class InMemoryTaskQueue implements TaskQueue {
	private final Queue<AgentTask> queue = new ConcurrentLinkedQueue<>();

	@Override
	public void push(AgentTask task) {
		queue.add(task);
	}

	@Override
	public Optional<AgentTask> poll() {
		return Optional.ofNullable(queue.poll());
	}

	@Override
	public int size() {
		return queue.size();
	}
}

