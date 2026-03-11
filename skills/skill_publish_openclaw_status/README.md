# skill_publish_openclaw_status

## Purpose
Publish Chimera availability/status events to the OpenClaw network using idempotent, retry-safe delivery.

## Invocation Context
Use after run completion or on scheduled health intervals to broadcast service status (`healthy`, `degraded`, `unavailable`).

## Input Contract (JSON)
```json
{
  "run_id": "run_20260310_110000",
  "target": "openclaw",
  "status_level": "healthy",
  "reason": "all_workflows_nominal",
  "idempotency_key": "chimera-status-2026-03-10T12:00:00Z"
}
```

### Input Rules
- `target` must be `openclaw`.
- `status_level` is one of: `healthy`, `degraded`, `unavailable`.
- `idempotency_key` is required and unique per logical status event.
- `run_id` may be null for purely scheduled heartbeat events.

## Output Contract (JSON)
```json
{
  "run_id": "run_20260310_110000",
  "publish_id": "pub_9981",
  "status": "PUBLISHED",
  "attempts": 1,
  "last_attempt_at": "2026-03-10T12:00:02Z",
  "response_code": 202,
  "errors": []
}
```

### Output Rules
- `status` is one of: `PUBLISHED`, `RETRYING`, `DEAD_LETTER`.
- `attempts` increments on every outbound delivery attempt.
- `errors` include actionable failure details when non-success.

## Exception Contract
- `VALIDATION_ERROR`: invalid status enum or missing idempotency key.
- `SOURCE_AUTH_FAILED`: invalid OpenClaw token/signature.
- `INTEGRATION_UNAVAILABLE`: OpenClaw endpoint unavailable.
- `INTERNAL_ERROR`: serialization/persistence failure.

## Side Effects
- Writes outbound event to `publish_event` outbox before sending.
- Updates publish state and retry metadata in persistence.
- Emits telemetry for success/failure rate and latency.

## Retry and Reliability Rules
- Retry on `429`, `5xx`, and network timeout.
- Backoff: `1s, 2s, 4s, 8s, 16s` with jitter.
- Max attempts: `5`, then move event to `DEAD_LETTER`.

## Non-Goals
- Does not fetch trend data.
- Does not calculate trend score.
