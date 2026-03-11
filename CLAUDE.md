# Chimera AI Co-Pilot Rules

## Project Context
"This is Project Chimera, an autonomous influencer system."

## Prime Directive
"NEVER generate code without checking specs/ first."

## Java-Specific Directives
"Strictly adhere to Java 21+ idioms. Utilize Records for immutable data transfer objects (DTOs) passing between Planner/Worker/Judge. Use JUnit 5 for all generated tests."

## Traceability
"Explain your plan before writing code."

## Mandatory Workflow
1. Read and align with `specs/_meta.md`, `specs/functional.md`, and `specs/technical.md` before implementation.
2. Map each code change to at least one functional story and one technical contract.
3. State assumptions, constraints, and acceptance criteria before editing files.
4. Generate or update tests with every behavior change.
5. Prefer incremental, reviewable commits with clear messages.

## Engineering Guardrails
- Do not introduce endpoints, schemas, or workflows not represented in `specs/` unless the spec is updated first.
- Favor explicit types and immutable records for request/response DTOs.
- Keep external integrations behind adapters and typed contracts.
- Use structured logs with `trace_id` and `run_id` for observability.
- Treat partial-source failures as recoverable unless all critical sources fail.

## Testing Guardrails
- Write tests first for new contracts whenever possible.
- Keep contract tests strict for JSON field names and types.
- Validate error payloads against the unified error schema.
- Add negative tests for timeouts, validation failures, and integration outages.

## Security and Reliability Guardrails
- Never hardcode secrets or tokens; use environment variables or secret management.
- Enforce idempotency for publish/retry workflows.
- Use retry with bounded exponential backoff for transient external failures.
- Preserve audit trails for admin-triggered or external publish operations.
