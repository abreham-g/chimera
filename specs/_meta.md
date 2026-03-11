# Project Chimera Meta Specification

## Vision
Project Chimera is an agentic trend-intelligence service that discovers short-form video trends, normalizes metadata across sources, and publishes operational status to an external network (OpenClaw). The system is designed to support fast decision-making for creators and content operations teams while maintaining reliable, observable, and auditable agent behavior.

## Problem Statement
Trend data is fragmented across multiple platforms, each with different metadata shapes, ranking signals, and update frequencies. Teams currently spend too much time manually collecting and cleaning data, leading to stale insights and poor planning decisions. Chimera addresses this by automating collection, enrichment, scoring, and publishing workflows through MCP-enabled agents.

## Scope (In)
- Collect trending video signals from configured sources.
- Normalize and store video metadata and trend snapshots.
- Provide agent-facing contracts for fetch, enrich, and score workflows.
- Expose service APIs for querying trends and triggering collection runs.
- Track run history, failures, and publish attempts for traceability.
- Publish Chimera availability/status to OpenClaw (bonus path).

## Scope (Out)
- Full production UI or dashboard implementation.
- Full historical backfill for all platforms.
- Advanced ML ranking model training in Day 2 scope.
- Multi-region active-active deployment.
- Billing, quota management, or enterprise tenancy.

## Constraints
- Java 21+ runtime.
- Maven-based build and dependency management.
- Spring Boot service foundation.
- MCP Java SDK integration for agent tool contracts.
- Time-boxed challenge delivery with incremental milestones.
- External APIs may be rate-limited or unstable.

## Assumptions
- Source APIs return enough metadata to identify trend movement.
- A relational database is available (PostgreSQL recommended).
- Network credentials for OpenClaw are provisioned for integration tests.
- Agent orchestration can run as scheduled or manually triggered jobs.

## Success Metrics
- P0 ingestion success rate: >= 98% per scheduled run.
- Freshness SLA: latest trend snapshot available <= 10 minutes after run start.
- Query latency p95 for trend endpoint: <= 500 ms for last 24h window.
- Status publish success rate to OpenClaw: >= 99% daily.
- Mean time to detect failed run via telemetry: <= 5 minutes.

## Risks and Mitigations
- Source API schema drift.
  - Mitigation: versioned source adapters and contract tests per provider.
- Rate-limit and transient external failures.
  - Mitigation: bounded retries, backoff, and run-level partial success handling.
- Duplicate or inconsistent video identities across sources.
  - Mitigation: canonical ID strategy plus deduplication rules.
- Agent hallucination or invalid tool output.
  - Mitigation: strict JSON schema validation and reject-on-invalid contracts.
- OpenClaw endpoint outages.
  - Mitigation: outbox table, retry queue, idempotency keys, and dead-letter policy.

## Day 2 Readiness Criteria
- All P0 user stories have acceptance criteria and mapped contracts.
- Database schema supports both metadata storage and operational telemetry.
- Error model is consistent across MCP tools and REST APIs.
- OpenClaw status publish flow is defined end-to-end.
