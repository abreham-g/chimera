package com.tenx.chimera_agent.api;

// File purpose: Defines RunsController behavior for Project Chimera.

// DTO for triggering a collection run.
import com.tenx.chimera_agent.api.contract.CollectRunRequest;
// DTO returned after a collection run is queued.
import com.tenx.chimera_agent.api.contract.CollectRunResponse;
// DTO for triggering a rerun request.
import com.tenx.chimera_agent.api.contract.RerunRequest;
// DTO returned after a rerun is queued.
import com.tenx.chimera_agent.api.contract.RerunResponse;
// Lightweight run registry used to track known runs.
import com.tenx.chimera_agent.run.RunRegistryService;
// Planner orchestrates goal/task creation for the swarm pipeline.
import com.tenx.chimera_agent.swarm.service.PlannerService;
// Enables bean validation for request payloads.
import jakarta.validation.Valid;
// Used for endpoint response status declarations.
import org.springframework.http.HttpStatus;
// Binds path variables like /runs/{runId}.
import org.springframework.web.bind.annotation.PathVariable;
// Declares POST endpoints.
import org.springframework.web.bind.annotation.PostMapping;
// Binds request body JSON to Java records.
import org.springframework.web.bind.annotation.RequestBody;
// Sets base route for controller endpoints.
import org.springframework.web.bind.annotation.RequestMapping;
// Declares fixed response status per endpoint.
import org.springframework.web.bind.annotation.ResponseStatus;
// Marks this class as a REST controller.
import org.springframework.web.bind.annotation.RestController;

// Used for converting platform list into planner goals.
import java.util.List;

// Exposes run lifecycle endpoints (collect + rerun) under /api/v1/runs.
@RestController
@RequestMapping("/api/v1/runs")
public class RunsController {
	// Service that creates planner tasks from high-level goals.
	private final PlannerService plannerService;
	// Service that keeps track of known run IDs and rerun relationships.
	private final RunRegistryService runRegistryService;

	// Constructor injection for required services.
	public RunsController(PlannerService plannerService, RunRegistryService runRegistryService) {
		// Store planner dependency for collect flow.
		this.plannerService = plannerService;
		// Store run registry dependency for collect/rerun tracking.
		this.runRegistryService = runRegistryService;
	}

	// Queues a new collect run and returns run metadata.
	@PostMapping("/collect")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public CollectRunResponse triggerCollectRun(@Valid @RequestBody CollectRunRequest request) {
		// Register and persist the run identity first.
		String runId = runRegistryService.registerCollectRun(request);
		// Translate requested platforms into planner goals and enqueue tasks.
		plannerService.planGoals(toPlannerGoals(request.platforms()));
		// Return a 202 response payload aligned with API contract.
		return new CollectRunResponse(runId, "QUEUED");
	}

	// Queues a rerun for an existing run ID.
	@PostMapping("/{runId}/rerun")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public RerunResponse rerun(@PathVariable("runId") String runId, @Valid @RequestBody RerunRequest request) {
		// Validate source run existence and register new rerun.
		String newRunId = runRegistryService.registerRerun(runId, request.reason());
		// Return rerun mapping source -> new run.
		return new RerunResponse(runId, newRunId, "QUEUED");
	}

	// Converts each platform into a planner-readable goal string.
	private List<String> toPlannerGoals(List<String> platforms) {
		// Build one collect-goal per platform.
		return platforms.stream()
				// Example: "youtube_shorts" -> "Collect trends from youtube_shorts".
				.map(platform -> "Collect trends from " + platform)
				// Materialize stream into immutable list.
				.toList();
	}
}

