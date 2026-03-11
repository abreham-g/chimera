#!/usr/bin/env python3
"""Basic spec/code alignment checks for Project Chimera."""

from pathlib import Path
import sys


REQUIRED_FILES = [
    "specs/_meta.md",
    "specs/functional.md",
    "specs/technical.md",
    "specs/openclaw_integration.md",
    "CLAUDE.md",
    "chimera-agent/chimera-agent/tests/trendFetcherTest.java",
    "chimera-agent/chimera-agent/tests/skillsInterfaceTest.java",
]


REQUIRED_SNIPPETS = {
    "specs/technical.md": [
        "## 3. MCP Tool Contracts (Agent-Facing)",
        "### 4.1 GET `/api/v1/trends`",
        "## 6. Database Schema",
    ],
    "specs/openclaw_integration.md": [
        "chimera.status.v1",
        "idempotency_key",
        "DEAD_LETTER",
    ],
    "CLAUDE.md": [
        "This is Project Chimera, an autonomous influencer system.",
        "NEVER generate code without checking specs/ first.",
        "Explain your plan before writing code.",
    ],
    "chimera-agent/chimera-agent/tests/trendFetcherTest.java": [
        "TrendsResponse",
        "TrendItem",
    ],
    "chimera-agent/chimera-agent/tests/skillsInterfaceTest.java": [
        "BudgetExceededException",
        "SkillFetchTrends",
        "SkillPublishOpenclawStatus",
    ],
}


def read_text(path: Path) -> str:
    return path.read_text(encoding="utf-8")


def main() -> int:
    repo_root = Path(__file__).resolve().parents[1]
    failures = []

    for rel_path in REQUIRED_FILES:
        file_path = repo_root / rel_path
        if not file_path.exists():
            failures.append(f"Missing required file: {rel_path}")

    for rel_path, snippets in REQUIRED_SNIPPETS.items():
        file_path = repo_root / rel_path
        if not file_path.exists():
            continue
        content = read_text(file_path)
        for snippet in snippets:
            if snippet not in content:
                failures.append(
                    f"Missing required snippet in {rel_path}: {snippet}"
                )

    if failures:
        print("SPEC CHECK FAILED")
        for failure in failures:
            print(f"- {failure}")
        return 1

    print("SPEC CHECK PASSED")
    print("- All required spec and governance files exist.")
    print("- Required API/tooling/spec snippets are present.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
