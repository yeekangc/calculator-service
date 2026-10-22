package com.example.mcp;

import com.example.service.CalculatorService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.mcpjava.server.tools.Tool;
import org.mcpjava.server.tools.ToolArg;

/**
 * Exposes the four calculator operations as MCP tools so that
 * any MCP-compatible AI agent can invoke them via the endpoint
 * published at /calculator-service/mcp.
 */
@ApplicationScoped
public class CalculatorMcpTools {

    @Inject
    CalculatorService calculatorService;

    @Tool(
        name = "add",
        title = "Add two numbers",
        description = "Returns the sum of two numbers a and b.",
        annotations = @Tool.Annotations(readOnlyHint = true, idempotentHint = true, openWorldHint = false)
    )
    public double add(
            @ToolArg(name = "a", description = "First operand") double a,
            @ToolArg(name = "b", description = "Second operand") double b) {
        return calculatorService.add(a, b);
    }

    @Tool(
        name = "subtract",
        title = "Subtract two numbers",
        description = "Returns the difference a minus b.",
        annotations = @Tool.Annotations(readOnlyHint = true, idempotentHint = true, openWorldHint = false)
    )
    public double subtract(
            @ToolArg(name = "a", description = "Minuend") double a,
            @ToolArg(name = "b", description = "Subtrahend") double b) {
        return calculatorService.subtract(a, b);
    }

    @Tool(
        name = "multiply",
        title = "Multiply two numbers",
        description = "Returns the product of two numbers a and b.",
        annotations = @Tool.Annotations(readOnlyHint = true, idempotentHint = true, openWorldHint = false)
    )
    public double multiply(
            @ToolArg(name = "a", description = "First factor") double a,
            @ToolArg(name = "b", description = "Second factor") double b) {
        return calculatorService.multiply(a, b);
    }

    @Tool(
        name = "divide",
        title = "Divide two numbers",
        description = "Returns the quotient of a divided by b. Returns an error if b is zero.",
        annotations = @Tool.Annotations(readOnlyHint = true, idempotentHint = true, openWorldHint = false)
    )
    public double divide(
            @ToolArg(name = "a", description = "Dividend") double a,
            @ToolArg(name = "b", description = "Divisor — must not be zero") double b) {
        return calculatorService.divide(a, b);
    }
}
