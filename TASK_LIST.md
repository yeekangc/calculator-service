# Project Plan: calculator-service

## App Description
A REST service for calculator operations.

## App Type
REST API

## Technical Choices
- Build tool: Maven
- Java version: 17
- Jakarta EE version: 11.0
- MicroProfile version: 7.1

## Advanced Options
- Database: none
- Security: none
- Testing: unit only

## Project Coordinates
- Directory: /Users/ykchang/repositories/lab-1457-test/calculator-service
- groupId: com.example
- artifactId: calculator-service

## Implementation Tasks

| # | Task ID | Description | Skill | Status |
|---|---|---|---|---|
| 1 | task-1-cdi-calculator-service | Implement CDI bean with add, subtract, multiply, divide logic | cdi | DONE |
| 2 | task-2-jaxrs-calculator-api | Expose JAX-RS REST endpoints for each calculator operation | jaxrs | DONE |
| 3 | task-3-mp-health-liveness | Add MicroProfile Health liveness and readiness checks | mp-health | DONE |
| 4 | task-4-mpOpenAPI-docs | Annotate endpoints with MicroProfile OpenAPI for API docs | mpOpenAPI | DONE |
