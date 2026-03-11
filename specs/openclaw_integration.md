# Project Chimera OpenClaw Integration Specification

## 1. Integration Goal
Chimera publishes machine-readable service status so OpenClaw participants can decide whether Chimera is healthy, degraded, or unavailable for downstream routing and collaboration.

## 2. Status Model

### 2.1 Status Levels
- `healthy`: all critical workflows meeting SLOs.
- `degraded`: partial failures or SLO breach, core service still running.
- `unavailable`: critical workflows down or publishing disabled.

### 2.2 Event Type
- Event name: `chimera.status.v1`
- Versioning rule: additive fields keep `v1`; breaking changes bump major.

## 3. Payload Contract

```json
{
  "event_type": "chimera.status.v1",
  "event_id": "evt_20260310_120000_01",
  "timestamp": "2026-03-10T12:00:00Z",
  "producer": {
    "service": "chimera-agent",
    "version": "0.1.0",
    "environment": "challenge"
  },
  "status": {
    "level": "healthy",
    "reason": "all_workflows_nominal",
    "uptime_seconds": 86400
  },
  "capacity": {
    "queue_depth": 4,
    "active_runs": 1,
    "max_concurrent_runs": 5
  },
  "recent_run_summary": {
    "window_minutes": 60,
    "total_runs": 6,
    "successful_runs": 6,
    "failed_runs": 0,
    "p95_run_duration_ms": 28000
  },
  "idempotency_key": "chimera-status-2026-03-10T12:00:00Z"
}
```

## 4. Transport and Scheduling
- Transport: HTTPS `POST` to OpenClaw status endpoint.
- Frequency: every 5 minutes plus on important state transition.
- Timeout policy: 3 seconds connect, 5 seconds read.
- Payload encoding: `application/json; charset=utf-8`.

## 5. Authentication and Authorization
- Primary auth: bearer token in `Authorization` header.
- Optional hardening: detached request signature in `X-Chimera-Signature`.
- Token rotation: at least every 30 days or per OpenClaw policy.
- Secret storage: environment variable or secret manager only.

## 6. Delivery Semantics
- Delivery guarantee: at-least-once.
- Idempotency: `idempotency_key` must be unique per logical status event.
- Retry policy:
  - Retries on `429`, `5xx`, and network timeouts.
  - Exponential backoff with jitter: 1s, 2s, 4s, 8s, 16s.
  - Max attempts: 5.
- Dead-letter rule:
  - After max attempts, mark event `DEAD_LETTER`.
  - Keep failure reason and latest response snapshot for triage.

## 7. Outbox Design
- Persist status events in `publish_event` table before send.
- Publisher worker reads `PENDING` events and attempts delivery.
- On success, mark `PUBLISHED` with `last_attempt_at`.
- On retryable failure, increment `attempts` and schedule next attempt.
- On terminal failure, mark `DEAD_LETTER`.

## 8. Operational Controls
- Circuit breaker opens when 5 consecutive publish attempts fail.
- While open, only one probe attempt is allowed every 60 seconds.
- Health endpoint should expose publish subsystem state (`CLOSED`, `OPEN`, `HALF_OPEN`).
- Alerts:
  - `DEAD_LETTER` count > 0 for 15 minutes.
  - publish success rate < 99% over 1 hour.
  - no published status event in last 15 minutes.

## 9. Observability and Audit
- Log fields: `publish_id`, `event_id`, `idempotency_key`, `attempt`, `http_status`, `trace_id`.
- Metrics:
  - `chimera_publish_attempt_total{status=...}`
  - `chimera_publish_latency_ms`
  - `chimera_publish_dead_letter_total`
- Audit retention: keep publish logs and events for at least 180 days.

## 10. Validation Rules
- Required fields: `event_type`, `event_id`, `timestamp`, `status.level`, `idempotency_key`.
- Reject publish if timestamp skew > 10 minutes from system clock.
- Reject status level outside allowed enum.
- Reject payloads larger than 32 KB.

## 11. Testing Plan
- Unit tests for payload builder and enum validation.
- Integration tests with mock OpenClaw endpoint:
  - success on first try
  - retry on 500
  - dead-letter after max retries
- Contract test for required JSON fields and value types.
