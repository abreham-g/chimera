package com.tenx.chimera_agent.swarm.model;

// File purpose: Defines TaskContext behavior for Project Chimera.

import java.util.List;
import java.util.Objects;

public record TaskContext(
		String goalDescription,
		List<String> personaConstraints,
		List<String> requiredResources
) {
	public TaskContext {
		Objects.requireNonNull(goalDescription, "goalDescription");
		personaConstraints = List.copyOf(Objects.requireNonNull(personaConstraints, "personaConstraints"));
		requiredResources = List.copyOf(Objects.requireNonNull(requiredResources, "requiredResources"));
	}
}

