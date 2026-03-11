# Project Chimera Architecture Strategy

## Objective
Define an implementation strategy that aligns the challenge requirements and SRS with a practical phased rollout.

## Recommended Agent Pattern
- Pattern: Hierarchical Swarm (`Planner -> Worker -> Judge`) with queue-based decoupling.
- Why:
  - Isolates planning, execution, and governance concerns.
  - Supports horizontal scale for workers without coupling to planner throughput.
  - Makes HITL escalation and quality controls explicit in the Judge stage.

## Human-in-the-Loop (HITL) Strategy
- Decision authority:
  - High confidence (`> 0.90`): auto-approve.
  - Medium confidence (`0.70 - 0.90`): escalate to async review queue.
  - Low confidence (`< 0.70`): reject and retry planning.
- Mandatory escalation:
  - Sensitive topics (politics, health, finance, legal) always require human review.

## Data Storage Strategy (SQL vs NoSQL)
- Primary operational store: PostgreSQL (SQL).
  - Best for transactional integrity, run history, audit logs, and relational joins.
  - Fits required schemas for `agent_run`, `trend_snapshot`, `publish_event`, and constraints.
- Memory/search store: Weaviate (vector/NoSQL behavior).
  - Best for semantic memory retrieval and persona evolution workflows.
- Cache/queue store: Redis.
  - Best for low-latency queue semantics and short-lived episodic context.

## Swarm Service Boundaries
- Planner Service:
  - Reads active goals + current state.
  - Creates immutable `AgentTask` records and enqueues them.
- Worker Service:
  - Pulls tasks and executes MCP-backed actions.
  - Emits `WorkerResult` records to review queue.
- Judge Service:
  - Applies confidence policy and safety checks.
  - Commits approved output using OCC (`state_version` guard).

## Concurrency and OCC
- Concurrency model: Java 21 virtual threads for high worker parallelism.
- Consistency model: Optimistic Concurrency Control in Judge commit stage.
  - Worker results carry `expectedStateVersion`.
  - Judge compares expected/current versions before commit.
  - Mismatch triggers conflict handling and planner re-evaluation.

## Governance and Traceability
- Rule source of truth: `CLAUDE.md` and `specs/*`.
- Build governance:
  - CI runs `make spec-check` and `make test` on push/PR.
  - CodeRabbit policy checks spec alignment, thread safety, and security.

## Phased Delivery Plan
1. Phase 1 (Core Swarm):
  - Implement planner/worker/judge services, task/review queues, OCC.
  - Add deterministic tests for flow and conflict behavior.
2. Phase 2 (MCP Perception/Action):
  - Add MCP client/resource polling and tool adapters.
3. Phase 3 (Memory + Persona):
  - Add `SOUL.md` loading, Redis episodic context, Weaviate semantic retrieval.
4. Phase 4 (HITL + Dashboard APIs):
  - Implement review queue APIs and approval/rejection endpoints.
5. Phase 5 (Agentic Commerce):
  - Add wallet operations and budget governor checks.
