# skill_fetch_trends

## Purpose
Fetch trending video signals from configured platforms, normalize core metadata, and return a contract-valid response for downstream scoring.

## Invocation Context
Use when Chimera needs to collect the latest trends for one or more sources as part of a scheduled or manual run.

## Input Contract (JSON)
```json
{
  "run_id": "run_20260310_110000",
  "platforms": ["youtube_shorts", "tiktok"],
  "window_minutes": 60,
  "limit_per_platform": 100
}
```

### Input Rules
- `run_id` is required and must be unique per workflow invocation.
- `platforms` must include at least one supported source.
- `window_minutes` must be in range `1..240`.
- `limit_per_platform` must be in range `1..500`.

## Output Contract (JSON)
```json
{
  "run_id": "run_20260310_110000",
  "status": "SUCCESS_PARTIAL",
  "collected_count": 167,
  "failed_platforms": ["tiktok"],
  "items": [
    {
      "platform": "youtube_shorts",
      "platform_video_id": "abc123",
      "title": "How to automate trend capture",
      "creator_handle": "@creator",
      "published_at": "2026-03-10T10:15:00Z",
      "views": 125000,
      "likes": 7100,
      "comments": 450,
      "shares": 300
    }
  ],
  "errors": [
    {
      "code": "SOURCE_TIMEOUT",
      "message": "Timeout while calling platform endpoint",
      "retryable": true
    }
  ]
}
```

### Output Rules
- `status` is one of: `SUCCESS`, `SUCCESS_PARTIAL`, `FAILED`.
- `items` contain canonicalized fields only.
- `errors` must follow unified error schema conventions from `specs/technical.md`.

## Exception Contract
- `VALIDATION_ERROR`: invalid input range, unknown platform.
- `SOURCE_AUTH_FAILED`: invalid/expired credentials.
- `SOURCE_TIMEOUT`: external API timeout (retryable).
- `INTEGRATION_UNAVAILABLE`: upstream service unavailable.

## Side Effects
- Persists raw and normalized trend records.
- Emits run-level telemetry (`run_id`, counts, duration).

## Non-Goals
- Does not compute final trend score.
- Does not publish status to OpenClaw.
