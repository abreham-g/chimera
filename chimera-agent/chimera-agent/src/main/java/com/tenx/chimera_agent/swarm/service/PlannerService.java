package com.tenx.chimera_agent.swarm.service;

// File purpose: Defines PlannerService behavior for Project Chimera.

import com.tenx.chimera_agent.swarm.model.AgentTask;
import com.tenx.chimera_agent.swarm.model.GlobalState;
import com.tenx.chimera_agent.swarm.queue.TaskQueue;
import com.tenx.chimera_agent.swarm.state.GlobalStateRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PlannerService {
	private final TaskQueue taskQueue;
	private final GlobalStateRepository globalStateRepository;

	public PlannerService(TaskQueue taskQueue, GlobalStateRepository globalStateRepository) {
		this.taskQueue = taskQueue;
		this.globalStateRepository = globalStateRepository;
	}

	public List<AgentTask> planGoals(List<String> goals) {
		Objects.requireNonNull(goals, "goals");
		GlobalState state = globalStateRepository.getCurrentState();

		List<AgentTask> tasks = goals.stream()
				.filter(goal -> goal != null && !goal.isBlank())
				.map(goal -> AgentTask.createGoalTask(goal.trim(), state.version()))
				.toList();

		tasks.forEach(taskQueue::push);
		return tasks;
	}
}

