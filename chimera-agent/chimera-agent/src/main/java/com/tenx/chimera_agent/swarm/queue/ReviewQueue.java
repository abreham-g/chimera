package com.tenx.chimera_agent.swarm.queue;

// File purpose: Defines ReviewQueue behavior for Project Chimera.

import com.tenx.chimera_agent.swarm.model.WorkerResult;

import java.util.Optional;

public interface ReviewQueue {
	void push(WorkerResult result);

	Optional<WorkerResult> poll();

	int size();
}

