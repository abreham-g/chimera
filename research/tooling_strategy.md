# Chimera Tooling Strategy (Day 2 Task 3.2)

## Objective
Define the developer MCP toolchain used to build Project Chimera safely and with strong traceability.

## Tool Categories
- Developer Tools (MCP): help engineers and coding agents inspect, edit, test, and govern the repository.
- Runtime Skills: domain capabilities executed by Chimera at runtime (documented in `skills/`).

## Selected MCP Servers (Developer Tooling)

### 1. Tenx MCP Sense (Required)
- Purpose: telemetry, traceability, and review of agent reasoning/actions.
- Why: challenge requirement and auditability of development decisions.
- Configuration note: keep connected during all coding sessions with the same account used for final submission.

### 2. Filesystem MCP
- Purpose: safe file navigation, read/write operations, and repository context gathering.
- Why: deterministic file edits and repeatable project manipulation.

### 3. Git MCP
- Purpose: staged diffs, commit inspection, and branch-aware code review.
- Why: commit hygiene and story-based delivery traceability.

### 4. GitHub MCP (optional but recommended)
- Purpose: issue/PR metadata, workflow checks, and remote repo governance.
- Why: aligns with Day 2 CI/CD and policy enforcement requirements.

## Baseline Configuration Template
Use this as a reference in IDE MCP settings (`mcp.json` or extension config format).

```json
{
  "mcpServers": {
    "tenx-sense": {
      "transport": "sse",
      "url": "https://<TENX_MCP_SENSE_ENDPOINT>/sse"
    },
    "filesystem": {
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-filesystem", "."]
    },
    "git": {
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-git", "."]
    },
    "github": {
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-github"],
      "env": {
        "GITHUB_TOKEN": "${GITHUB_TOKEN}"
      }
    }
  }
}
```

## Operational Rules
- Keep Tenx MCP Sense connected before opening coding tasks.
- Use Filesystem MCP for edits and readback verification.
- Use Git MCP before and after major changes to keep commit history clear.
- Use GitHub MCP once CI workflow and code-review policy files are added.

## Security and Access Notes
- Do not commit MCP tokens or secrets to the repository.
- Store credentials in local environment or secret manager.
- Restrict MCP server scopes to least privilege for challenge work.

## Validation Checklist
- MCP Sense connection visible and logging.
- Filesystem MCP can read and write repo files.
- Git MCP can show status and history.
- (Optional) GitHub MCP can list repository metadata with valid token.
