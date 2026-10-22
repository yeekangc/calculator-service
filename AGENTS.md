# Agent Instructions for calculator-service

This file provides guidance to agents working in this Liberty project.

## Project Overview

- **Project name**: `calculator-service`
- **Project path**: `/Users/ykchang/repositories/lab-1457-test/calculator-service`
- **Build tool**: `Maven`
- **Context root**: `/calculator-service`
- **HTTP port**: `9080`
- **HTTPS port**: `9443`
- **Development pattern**: `test-driven-inner-loop`

## Platform

<!-- Resolved from TASK_LIST.md during generate_agents_md — do not edit manually -->
- Jakarta EE: **11.0**
- MicroProfile: **7.1**

> **Feature skills use this section** to resolve correct Liberty feature version strings.
> Each feature skill contains an inline mapping table — look up your Jakarta EE or MicroProfile
> version there to get the exact `<feature>` string to add to `server.xml`.

This project uses Open Liberty development workflows managed by the **Liberty Dev Mode Manager (LDM)**.

## MCP Responsibility Split

**Two MCP servers work together in this project. Use each for its intended role:**

| Responsibility | MCP Server |
|---|---|
| Start / stop dev mode, console output, test triggers, monitoring | **LDM** (`liberty_*` tools) |
| Jakarta EE / MicroProfile feature guidance, skills | **liberty-tools-mcp** (`get_liberty_skills`) |
| Read messages.log, server.xml, any project file offline | **liberty-tools-mcp** (`read_liberty_log`, `read_server_xml`, `read_project_file`) |
| Onboard project (inject Liberty plugin into build file) | **liberty-tools-mcp** (`patch_build_file`) |
| Generate project-specific AGENTS.md | **liberty-tools-mcp** (`generate_agents_md`) |

> **Key rule**: For all Liberty runtime operations — starting dev mode, watching build output, running tests, reading live console output — use the LDM tools. For implementing Jakarta EE / MicroProfile features, always load the relevant skill first with `get_liberty_skills`.

## Application Requirements

<!-- BEGIN TEMPLATED SECTION: Application Requirements -->
<!-- This section is extracted from the project plan document (TASK_LIST.md, PLAN.md, README.md, or REQUIREMENTS.md) -->
**Source of Truth**: `TASK_LIST.md`

Before implementing features, review the project plan for:
- Application purpose and business requirements
- API endpoints and their specifications
- Domain model and data structures
- Testing scenarios and acceptance criteria
- Feature priorities and implementation order

### Application Overview

A REST service for calculator operations.

<!-- END TEMPLATED SECTION -->

## Enabled Features

- **Web Application Packaging**: WAR structure, URL construction, static resources, web.xml requirements.
- **JAX-RS**: REST APIs, request handling, validation, and exception mapping.
- **Servlet**: Servlets, filters, sessions, and low-level HTTP handling.
- **CDI**: Dependency injection, scopes, producers, and lifecycle patterns.
- **JPA**: Persistence, entities, repositories, and transactions.
- **MicroProfile Health**: Readiness and liveness health checks.
- **MicroProfile OpenAPI**: Generated API documentation and schema guidance.

## Feature Selection Best Practices

**Prefer minimal features over convenience features:**
- ✅ Baseline: `servlet-6.0` only — lets Liberty deploy the WAR and start dev mode cleanly
- ✅ Add specific features per task: `restfulWS-3.1`, `cdi-4.0`, `jsonb-3.0`, etc.
- ❌ Avoid broad convenience features: `jakartaee-10.0`, `jakartaee-11.0`, `microProfile-7.1`

**Why minimal features are better:**
- `servlet-6.0` alone starts Liberty in 4–7 seconds with no feature downloads
- Broad platform features (`jakartaee-11.0`) trigger a 15-minute install on first run
- Without `servlet-6.0`, Liberty cannot deploy the WAR → dev mode times out → stale lock
- Task-by-task feature addition keeps startup fast throughout the inner loop

## Mandatory Workflow: Skills First

Before implementing any Liberty framework feature or workflow, **always** call `get_liberty_skills` first and follow the returned guidance.

**To list all available skills:**
```
get_liberty_skills(skill="list")
```

### Feature-to-Skill Routing

<!-- BEGIN TEMPLATED SECTION: Feature-to-Skill Routing Rules -->
| Feature | Skill | Use Case |
|---------|-------|----------|
| Web Application Packaging | `webapp-packaging` | WAR structure, URL construction, static resources, web.xml requirements. |
| JAX-RS | `jaxrs` | REST APIs, request handling, validation, and exception mapping. |
| Servlet | `servlet` | Servlets, filters, sessions, and low-level HTTP handling. |
| CDI | `cdi` | Dependency injection, scopes, producers, and lifecycle patterns. |
| JPA | `jpa` | Persistence, entities, repositories, and transactions. |
| MicroProfile Health | `mp-health` | Readiness and liveness health checks. |
| MicroProfile OpenAPI | `mpOpenAPI` | Generated API documentation and schema guidance. |

<!-- END TEMPLATED SECTION -->

If the correct skill is unclear, call `get_liberty_skills(skill="list")` to see all options.

Only proceed without a skill if `get_liberty_skills` reports that the requested skill is not found.

---

## ⚠️ Mandatory Process Rules

These rules are not optional. Violating them produces unreliable results.

### Rule 1 — Greenfield Starter Baseline

> **This project is assumed to be generated from the Liberty starter. The starter-generated project is the runnable baseline. Do not create an additional MVP phase or synthetic entry point before starting dev mode. Start Liberty dev mode from the starter-generated baseline and begin incremental implementation immediately.**

The starter already provides:
- build file with Liberty Maven plugin
- `server.xml` with initial feature set
- deployable application structure
- baseline runtime viability in dev mode

There is no preliminary build phase needed. Go directly to dev mode.

> ℹ️ **This rule applies to the initial greenfield development session only.** If you are returning to an already-developed project in a new session, or the user wants to add enhancements beyond the original plan, see the **"Returning to Development / Adding Enhancements"** section below instead.

### Rule 2 — Dev Mode Owns the Inner Loop

> **After Liberty dev mode starts successfully, all implementation validation must go through Liberty dev mode via LDM. Offline test commands are prohibited for routine TDD verification.**

**Prohibited once dev mode is active:**
- ❌ `mvn test`
- ❌ `mvnw test`
- ❌ `./mvnw.cmd test`
- ❌ Any offline Maven test command

**Permitted exclusively:**
- ✅ LDM test-on-demand (trigger tests through LDM tools)
- ✅ Dev mode compile/reload observation via LDM console output
- ✅ Live endpoint verification **after** dev mode tests pass

Offline Maven tests are only appropriate if dev mode is not running and cannot be started.

### Rule 3 — LDM is Mandatory (Hard Stop)

> **Once dev mode begins and the inner loop is active, LDM is the required control plane. If LDM is unavailable, cannot reconnect to the running session, cannot identify the active instance, or cannot trigger/observe dev-mode tests — stop implementation work and ask the user to investigate.**

Do not fall back to offline substitutes when LDM is impaired. The workflow is broken until LDM is restored.

LDM is the control plane for:
- starting and stopping dev mode
- monitoring build/compile/reload output
- triggering tests on demand
- observing runtime state

### Rule 4 — Just-in-Time Skill Loading

> **Do not preload multiple Liberty feature skills. Load only the skill needed for the current implementation task, immediately before implementing that task.**

- ❌ Do not front-load multiple framework skills during setup
- ❌ Do not load skills for future tasks speculatively
- ✅ Identify the next task → load its skill → implement → test → move on
- ✅ **Exception**: Two skills may be loaded in the same turn only when they are both required for a single atomic implementation task (e.g. `cdi` + `jaxrs` when implementing a CDI-backed REST resource together). This exception does **not** extend across tasks.

Preloading multiple skills increases context noise and encourages batched implementation.

> **If context budget pressure is detected** (e.g. a warning that too many tool calls have been made), do **not** respond by batching skill loads or implementation steps. Instead: stop at the current task boundary, report what has been completed and what remains, and ask the user to continue in a new turn. Batching under pressure is the most common cause of Rule 4 violations.

### Rule 5 — Validation Source of Truth

> **When dev mode is active, the authoritative status of the application is determined exclusively in this order:**
> 1. Current dev mode compile/reload output
> 2. Current dev mode test results
> 3. Live endpoint verification — only after 1 and 2 succeed

**Do not rely on:**
- ❌ Stale spawn-log entries from earlier edits
- ❌ Previous successful endpoint responses
- ❌ Offline Maven test output
- ❌ Assumptions that a code change is active before dev mode confirms compilation/reload

If compilation has not been confirmed in dev mode output, no test result or endpoint response is authoritative.

### Rule 6 — Dev Mode Gate (Sequencing Mandate)

> **Starting dev mode is not optional. It is the required first action after reading this file. No feature skill may be loaded and no implementation file may be written until `liberty_start_devmode` has returned `status: STARTED` in the current session.**

This is a sequencing mandate, not a conditional: dev mode must be **attempted and confirmed** before any implementation work begins. Skipping startup and writing code directly is a Rule 6 violation regardless of whether the resulting code would work.

- ❌ Do not write any `.java`, `.xml`, `.properties`, or `.html` implementation file before `STARTED`
- ❌ Do not call `get_liberty_skills` for any feature skill before `STARTED`
- ❌ Do not skip startup because "the code can be written first and tested later"
- ✅ Follow **Development Workflow Step 1** — it defines the startup sequence.
- ✅ If dev mode fails to start: load `server-wont-start` skill, resolve the failure, confirm `STARTED` — then and only then begin the inner loop

### Rule 7 — webapp-packaging Skill Gate (Hard Stop)

> **Before creating ANY file anywhere under `src/main/webapp/` — including `WEB-INF/web.xml`,
> deployment descriptors, and configuration files — load the `webapp-packaging` skill.**
> ```
> get_liberty_skills(skill="webapp-packaging")
> ```

This rule applies to the **entire directory tree** rooted at `src/main/webapp/`, not just static resources. The loose WAR descriptor timing issue affects all files in this directory regardless of their purpose — a `WEB-INF/web.xml` created mid-session triggers the same restart requirement as an `index.html`.

The Liberty Maven plugin only registers `src/main/webapp` in the loose WAR descriptor at **initial startup**. If dev mode is already running when `src/main/webapp` is first created, Liberty will not serve files from it until the WAR is fully rebuilt and dev mode is restarted.

**Correct sequence when adding static resources mid-session:**
1. Load `webapp-packaging` skill
2. Stop dev mode: `liberty_stop_devmode(instanceId=...)`
3. Create `src/main/webapp/` and write static files
4. Start dev mode: `liberty_start_devmode(projectDir=..., buildTool=maven|gradle)`
5. Verify `http://localhost:9080/calculator-service/` returns HTML

**If dev mode is not yet started when `src/main/webapp` is first created**, the correct sequence is simply: create the directory + files → start dev mode normally.

Skipping this skill and creating `src/main/webapp` while dev mode is running causes a 404/spin that cannot be resolved by hot-reload alone.

### Rule 8 — Never Kill the LDM Daemon Directly

> **Do not terminate the LDM daemon process using `Stop-Process`, `kill`, `taskkill`, or any other shell command.**

The LDM daemon manages the MCP session. Killing it permanently breaks the `liberty_*` tool connection for the remainder of the session — there is no reconnect path once the daemon PID is gone.

**Permitted recovery for a stale in-memory lock** (the only scenario where a kill might feel necessary):
1. Edit `~/.ldm/projects.json` — set the array to `[]` and save.
2. Wait 10 seconds for the daemon's poll cycle to clear the stale entry.
3. Retry `liberty_start_devmode(projectDir=..., buildTool=maven|gradle)`.
4. Only if steps 1–3 fail after clearing the `.ldm` directory inside the project: ask the user to reload the VS Code window or restart the LDM daemon via the IDE — **do not attempt a PID kill yourself**.

If the MCP session is already lost (LDM tools return connection errors), stop all implementation work and ask the user to restore the LDM session before continuing.

### Rule 9 — Verify pom.xml After patch_build_file

> **After calling `patch_build_file`, always read the result with `read_project_file` and verify the XML structure is correct.**

```
read_project_file(projectPath="...", path="pom.xml")
```

Verify:
- `<ldmEnabled>true</ldmEnabled>` is present inside `<configuration>`
- `<configuration>` is nested inside `<plugin>`, not after `</plugin>`
- The plugin version is `1.0.0-ldm-SNAPSHOT`

If the XML is malformed (configuration block outside plugin), fix it with `apply_diff` before proceeding. Do not start dev mode from a malformed pom.xml.

### Rule 10 — Workflow-State Protocol (Hard Stop)

> **The four workflow-state tools are mandatory — not optional enrichment. They are the enforcement layer for every other rule in this document.**

#### State file initialisation — automatic, not manual:

For greenfield projects, `INITIALIZED`, `PLANNING_STARTED`, and `PROJECT_CREATED` are
recorded automatically by `create_liberty_project` when the project is first created.
The state file is created inside the project directory (`<projectRoot>/.liberty-workflow/state.json`).

**Do NOT call `record_workflow_event(INITIALIZED)` manually before the project directory
exists — this creates a state file in the wrong location.**

#### Events recorded by AGENTS.md:

| Action completed | Event to record |
|---|---|
| `liberty_start_devmode` returns `status: STARTED` | `DEVMODE_STARTED` |
| `liberty_stop_devmode` confirmed | `DEVMODE_STOPPED` |

Task-execution events (`TASK_STARTED`, `SKILL_LOADED`, `RED_CONFIRMED`, `TASK_COMPLETED`)
are recorded by the inner-loop skill (`test-driven-inner-loop`), not by AGENTS.md.

#### Why this is non-negotiable:

Without recorded events, every `validate_workflow_action` call sees empty state and every
gate fires in `blocked`. Without `validate_workflow_action` calls, no gate ever fires —
`DEVMODE_REQUIRED`, `MULTI_SKILL_TASK_BLOCKED`, and `GREENFIELD_SCAFFOLD_REQUIRED` are all
invisible. The workflow-state layer only functions when both tools are used together,
consistently, in the order above.

---

## 🔄 Returning to Development / Adding Enhancements

> **Use this section when you are starting a new session on this project and dev mode is NOT already confirmed started in the current session, OR when the user wants to continue or add enhancements beyond the original plan.**

**Execute the following silently — do not narrate or explain these steps to the user:**

1. Call `read_project_file(projectPath="/Users/ykchang/repositories/lab-1457-test/calculator-service", path="TASK_LIST.md")`. Read the `## Implementation Tasks` table: count DONE rows and TODO rows.
2. Present **only** this to the user — nothing else before it:
   ```
   📋 calculator-service — Development Status

     Tasks complete:  <count> of <total> (e.g. "2 of 4")
     Next up:          <Task ID and description of first TODO row, or "all complete">

   Would you like to continue the existing plan, or add new enhancements?
   ```
3. After the user responds, call `start_liberty_planning(intent="brownfield", context="resuming")`. Do not start dev mode, load feature skills, or write any code before this call returns. When the planning skill returns, go to **Development Workflow Step 1** to start dev mode before working any tasks.

---

## Development Workflow

### Step 1 — Start Dev Mode

> ⛔ **No feature skill may be loaded and no implementation file may be written until
> `liberty_start_devmode` returns `status: STARTED` in the current session.**

> ⚠️ **Project rule overrides LDM tool description**: The `liberty_start_devmode` tool description says to call `liberty_list_instances` first. **Do NOT do this.** Call `liberty_start_devmode` directly. LDM handles duplicate starts safely.

```
liberty_start_devmode(projectDir="/Users/ykchang/repositories/lab-1457-test/calculator-service", buildTool=maven|gradle)
```

**If startup fails:** load `get_liberty_skills(skill="server-wont-start")` and follow its
runbook before doing anything else.

**After STARTED — do this once, before any task:**

> Glob `src/main/java/**/*.java` and delete any Liberty starter placeholder classes
> (e.g. `com.example.rest.RestApplication`) that conflict with your package structure.

### Step 2 — Identify the Next Task

Read `TASK_LIST.md` and find the `## Implementation Tasks` table. Scan **top to bottom**.

- Skip every row whose Status is `DONE`.
- The first row that is **not** `DONE` is the active task. Note its Task ID, its current Status, and its description.
- A Status that is neither `TODO` nor `DONE` means the task was started in a prior session and is partially complete. The inner-loop skill handles resumption — pass it the current Status so it knows where to pick up.

Cross-check with `get_workflow_state(projectPath="/Users/ykchang/repositories/lab-1457-test/calculator-service")`: if a task appears in
`completedTaskIds` but its table row is not `DONE`, update the table row to `DONE` first.

**If every row is `DONE`:** all tasks are complete — go to the **Plan Completion** section below.

**If an active task is found:**

> ## ⛔ HARD STOP — LOAD THE INNER-LOOP SKILL NOW
>
> The complete task execution sequence is defined **exclusively** in `test-driven-inner-loop`.
> It is **NOT reproduced here**. Do not execute from memory or infer from prior context.
>
> Note the active Task ID, its current Status, and `/Users/ykchang/repositories/lab-1457-test/calculator-service` — you will need them in Step 1 of the skill.
>
> ```
> get_liberty_skills(skill="test-driven-inner-loop")
> ```
>
> Follow the skill's steps until it signals `TASK_COMPLETED`.
> Then return here to Step 2 and identify the next task.

### Plan Completion

When **every row** in the `## Implementation Tasks` table is `DONE`:

1. Verify the implementation is complete:
   - Read `server.xml` with `read_server_xml` — confirm a `<feature>` entry exists for every planned feature.
   - Run `liberty_run_tests` — confirm all tests pass.
   - If anything is missing, go back to Step 2 (a row may need its status corrected to TODO).

2. Present a brief summary to the user:
   ```
   ✅ Plan complete for calculator-service

     Tasks completed: <count>
     Tests:            All passing
     Features added:   <list from server.xml>
   ```

3. Ask exactly one question:
   > **"The plan is complete. Would you like to add more enhancements?"**

4. If yes → call `start_liberty_planning(intent="brownfield", context="resuming")`.
   The brownfield skill updates `TASK_LIST.md` with new tasks and regenerates `AGENTS.md`.
   When it returns, go back to Step 2.

5. If no → the session is complete. Dev mode may remain running for manual exploration.

6. If the user makes **any code-change request** without explicitly answering this question —
   treat it as "yes": add a row to `TASK_LIST.md` and go to Step 2. Do not apply the change
   directly.

---

### ⚠️ Rule — Every Code Change Goes Through the Task Table

Any user request that would result in modifying a source file — no matter how small —
must be treated as a new task:

1. Add a row to `TASK_LIST.md` with a new Task ID, description, skill, and Status `TODO`.
2. Return to **Step 2** (Identify the Next Task).
3. Load `test-driven-inner-loop` and follow it completely — even for a one-line change.

Do NOT apply the change directly. "Small" is not an exception to the inner loop. The TDD
loop exists precisely to catch the cases that feel trivial.

---

### Stopping Dev Mode (when a restart is required)

Call `liberty_stop_devmode(instanceId=...)` directly.

---

## Common Startup Issues

### Feature Installation Hangs
**Symptoms:** Server stuck at "Installing features: [...]" for several minutes

**Solutions:**
1. Stop dev mode (via LDM)
2. Edit `src/main/liberty/config/server.xml` to use minimal specific features (not `jakartaee-10.0`)
3. Start dev mode again
4. Watch build output for progress

### Server Won't Start
**First steps:**
1. Ask the agent to check LDM build output and spawn log for error messages
2. Use `read_liberty_log` to read `messages.log` after at least one startup attempt
3. Look for CWWK error codes (e.g., `CWWKE0701E`, `CWWKF1299E`)
4. Use `read_server_xml` to verify feature configuration

### Port Already in Use
**Solutions:**
1. Find and kill process using the port, or change ports in `server.xml`
2. `server.xml` port changes are hot-reloaded — no restart needed

---

## Success Indicators

### Server Started Successfully
- ✅ `CWWKF0011I: The defaultServer server is ready to run a smarter planet`
- ✅ `CWWKZ0001I: Application calculator-service started in X.XXX seconds`
- ✅ `CWWKT0016I: Web application available (default_host): http://localhost:9080/calculator-service/`
- ✅ `CWWKF0012I: The server installed the following features: [...]`
- ✅ No CWWK error codes in build output

### Feature Working Correctly
- ✅ Endpoints respond with expected HTTP status codes
- ✅ CDI injection working (no NullPointerException on `@Inject` fields)
- ✅ No exceptions in console during normal operation

### Development Mode Active
- ✅ Build output shows "Liberty is running in dev mode"
- ✅ File changes trigger automatic recompilation
- ✅ Application redeploys automatically after code changes

---

## Build and Test Commands

- Build (offline, before dev mode): `mvn clean package`
- Start Liberty in dev mode: ask the agent (LDM handles this via `liberty_start_devmode`)
- Trigger tests in dev mode: ask the agent to run tests (LDM handles this via `liberty_run_tests`)
- Stop dev mode: ask the agent (LDM handles this via `liberty_stop_devmode`)
- Read log: `liberty_read_log(projectDir="/Users/ykchang/repositories/lab-1457-test/calculator-service")`
- Read server.xml: `liberty_read_file(path="server.xml", projectDir="/Users/ykchang/repositories/lab-1457-test/calculator-service")`

> **Note**: Do not use `mvn test` or `mvnw test` for routine TDD verification while dev mode is running.
> All inner-loop test execution must go through LDM.

## Project Structure

- `pom.xml` — Maven build configuration
- `src/main/java` — application source code
- `src/main/resources` — application resources
- `src/main/liberty/config/server.xml` — Liberty server configuration

---

## Liberty Recovery Behavior After MCP Restart

If the LDM process restarts while Liberty is still running:
- Ask the agent to check for running Liberty instances via LDM
- LDM uses crash-resilient state files to reconnect to existing instances
- Do **not** assume the previous dev mode console stream is still attached
- If Liberty is no longer running, start dev mode again via LDM

If liberty-tools-mcp restarts, all 6 project-intelligence tools remain available immediately — this server holds no runtime state.

---

## Troubleshooting Guidance

- Ask the agent to check LDM build output before other diagnostic steps via `liberty_get_build_output`.
- Use `liberty_read_log` to read `messages.log` (works for live instances or offline projects).
- Use `liberty_read_file(path="server.xml")` to verify server configuration.
- Use `liberty_read_file` to inspect `pom.xml`, `build.gradle`, or any project file.
- After MCP restart, LDM handles reconnection to running Liberty instances.
- For REST endpoint issues, load `get_liberty_skills(skill="jaxrs")` before changing resources or exception mappers.
- For readiness/liveness issues, load `get_liberty_skills(skill="mp-health")` before changing health checks.

---

## Quick Reference

### Dev Mode Operations (via LDM)
- Start Liberty in dev mode
- Check server status / HTTP port
- Watch build output after code changes
- Trigger tests in dev mode
- Stop dev mode

### Project Intelligence (via liberty-tools-mcp)
```
# Read messages.log (works even when server is stopped)
read_liberty_log(projectPath="/Users/ykchang/repositories/lab-1457-test/calculator-service", projectName="calculator-service")

# Read server.xml
read_server_xml(projectPath="/Users/ykchang/repositories/lab-1457-test/calculator-service", projectName="calculator-service")

# Read any project file
read_project_file(projectPath="/Users/ykchang/repositories/lab-1457-test/calculator-service", path="pom.xml")

# List available skills
get_liberty_skills(skill="list")
```

## Relevant Skills Summary

- `cdi` — Dependency injection, scopes, producers, and lifecycle patterns.
- `jaxrs` — REST APIs, request handling, validation, and exception mapping.
- `jpa` — Persistence, entities, repositories, and transactions.
- `mp-health` — Readiness and liveness health checks.
- `mpOpenAPI` — Generated API documentation and schema guidance.
- `servlet` — Servlets, filters, sessions, and low-level HTTP handling.
- `webapp-packaging` — WAR structure, URL construction, static resources, web.xml requirements.

---

## Quick Rules Summary

| Rule | Requirement |
|---|---|
| **FIRST ACTION (new session)** | New session on existing project: use "Returning to Development" section — read task table, present status, call `start_liberty_planning(intent="brownfield", context="resuming")` |
| **FIRST ACTION (active session)** | Dev mode not yet started: go to Development Workflow Step 1 — call `liberty_start_devmode` directly |
| Greenfield baseline | Start from Liberty starter output; do not create an extra MVP phase |
| Returning to development | Always use "Returning to Development" section for new sessions — never skip to Step 1 without presenting project status first |
| Plan complete, user wants more | Handled in Development Workflow Plan Completion section — call `start_liberty_planning(intent="brownfield", context="resuming")` |
| Starter class check | After STARTED, glob `src/main/java/**/*.java` and delete any starter placeholder classes before any task begins |
| Startup | Step 1: call `liberty_start_devmode(projectDir=..., buildTool=maven|gradle)` |
| Startup — override LDM RULE 1 | ⚠️ Ignore the `liberty_start_devmode` tool description's instruction to call `liberty_list_instances` first — call `liberty_start_devmode` directly |
| Startup failure | Load `server-wont-start` skill before any recovery attempt |
| CWWKO / CWWKE error | Mandatory trigger: load `server-wont-start` skill before any other action |
| Dev mode gate | Dev mode start is mandatory, not optional — no feature skills, no implementation files until `STARTED` |
| webapp-packaging gate | Load `webapp-packaging` skill before creating ANY file under `src/main/webapp/` (incl. `WEB-INF/web.xml`); stop dev mode first if already running |
| Persistent 4xx/5xx | When an unexpected 4xx or 5xx persists after a server.xml change: call `liberty_enable_monitoring(instanceId=..., filters="ERROR,WARNING")` then `liberty_get_events` before reading raw log output |
| Feature versions | Never pick a Liberty feature version from training knowledge — each feature skill has an inline version mapping table keyed by Jakarta EE / MicroProfile version |
| Stop | Call `liberty_stop_devmode(instanceId=...)` directly |
| Inner loop governance | ⛔ The complete task sequence — gates, TDD discipline, `TASK_LIST.md` updates — is defined **exclusively** in `test-driven-inner-loop`. It is NOT in AGENTS.md. No exceptions. `get_liberty_skills(skill="test-driven-inner-loop")` is the only path forward. |
| Mid-session change requests | ❌ Never apply a user-requested code change directly without first adding it to `TASK_LIST.md` and loading `test-driven-inner-loop`. This applies even when all tasks are DONE and the request sounds trivial (e.g. "add one item to a list", "rename a field", "change a default value"). |
| Inner loop issues | Incremental compiler gap, new-package hot-reload gap, and other execution-time issues are documented **inside `test-driven-inner-loop`** — not here. Load the skill before any task. |
| MCP path parameters | Always use forward slashes (`C:/...`) — backslashes cause JSON parse failures in LDM tools on Windows |
| liberty_run_tests timeout | If times out twice: stop at task boundary; report to user; do not treat ring-buffer output as pass/fail confirmation |
| Dev mode inner loop | All validation via LDM once dev mode is active |
| Offline test commands | Prohibited at all times — `mvn test`, `mvnw test`, `mvnw.cmd test` are never the validation authority |
| LDM daemon | Never kill with Stop-Process/kill/taskkill; recovery via `~/.ldm/projects.json` edit or ask user to restart |
| LDM spawn guard stuck | If "already being started" persists after JVM is stopped: ask user to reload VS Code window — projects.json edit will not fix this |
| LDM unavailable | Stop implementation; ask user to restore LDM session; no offline fallback |
| Skill loading | Just-in-time per task; two skills allowed only if both required for a single atomic task |
| Context budget pressure | Stop at task boundary; report status; ask user to continue — never batch |
| Validation authority | Dev mode compile output → dev mode test results → endpoint (in that order) |
| No runtime claims | Do not state the app is running or accessible without an observed `CWWKF0011I` in the current session |
| pom.xml after patch | Read pom.xml after patch_build_file; verify `<configuration>` is inside `<plugin>` |
| Plan completion | Cross-check TASK_LIST.md key features against server.xml before declaring done |
| Stale signals | Spawn-log history, previous endpoint responses — not authoritative |
| Workflow-state init | State file created automatically by `create_liberty_project` — do NOT call `record_workflow_event(INITIALIZED)` manually before the project directory exists |
| Workflow-state events | AGENTS.md records: `DEVMODE_STARTED`, `DEVMODE_STOPPED`, planning events. Task events (`RED_CONFIRMED`, `GREEN_CONFIRMED`, etc.) are recorded by `test-driven-inner-loop`. |

---
Generated by Liberty Tools MCP.
