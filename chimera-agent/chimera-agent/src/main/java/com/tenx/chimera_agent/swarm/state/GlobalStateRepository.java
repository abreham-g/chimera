package com.tenx.chimera_agent.swarm.state;

// File purpose: Defines GlobalStateRepository behavior for Project Chimera.

import com.tenx.chimera_agent.swarm.model.GlobalState;

public interface GlobalStateRepository {
	GlobalState getCurrentState();

	boolean compareAndSet(long expectedVersion, GlobalState newState);
}

