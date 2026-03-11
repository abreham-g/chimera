package com.tenx.chimera_agent.swarm.queue;

// File purpose: Defines TaskQueue behavior for Project Chimera.

import com.tenx.chimera_agent.swarm.model.AgentTask;

import java.util.Optional;

public interface TaskQueue {
	void push(AgentTask task);

	Optional<AgentTask> poll();

	int size();
}

