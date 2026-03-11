package com.tenx.chimera_agent.swarm.state;

// File purpose: Defines InMemoryGlobalStateRepository behavior for Project Chimera.

import com.tenx.chimera_agent.swarm.model.GlobalState;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class InMemoryGlobalStateRepository implements GlobalStateRepository {
	private final AtomicReference<GlobalState> stateRef = new AtomicReference<>(GlobalState.initial());

	@Override
	public GlobalState getCurrentState() {
		return stateRef.get();
	}

	@Override
	public boolean compareAndSet(long expectedVersion, GlobalState newState) {
		GlobalState current = stateRef.get();
		if (current.version() != expectedVersion) {
			return false;
		}
		return stateRef.compareAndSet(current, newState);
	}
}

