package com.example.api;

import com.example.service.CalculatorService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/calculate")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Calculator", description = "Arithmetic calculator operations")
public class CalculatorResource {

    @Inject
    CalculatorService calculatorService;

    // Setter for unit testing
    void setCalculatorService(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @GET
    @Path("/add")
    @Operation(summary = "Add two numbers", description = "Returns the sum of a and b")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Sum of a and b")
    })
    public Response add(
            @Parameter(description = "First operand", required = true) @QueryParam("a") double a,
            @Parameter(description = "Second operand", required = true) @QueryParam("b") double b) {
        return Response.ok(new CalculationResult(calculatorService.add(a, b))).build();
    }

    @GET
    @Path("/subtract")
    @Operation(summary = "Subtract two numbers", description = "Returns the difference a minus b")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Difference of a minus b")
    })
    public Response subtract(
            @Parameter(description = "First operand", required = true) @QueryParam("a") double a,
            @Parameter(description = "Second operand", required = true) @QueryParam("b") double b) {
        return Response.ok(new CalculationResult(calculatorService.subtract(a, b))).build();
    }

    @GET
    @Path("/multiply")
    @Operation(summary = "Multiply two numbers", description = "Returns the product of a and b")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Product of a and b")
    })
    public Response multiply(
            @Parameter(description = "First operand", required = true) @QueryParam("a") double a,
            @Parameter(description = "Second operand", required = true) @QueryParam("b") double b) {
        return Response.ok(new CalculationResult(calculatorService.multiply(a, b))).build();
    }

    @GET
    @Path("/divide")
    @Operation(summary = "Divide two numbers", description = "Returns the quotient of a divided by b. Returns 400 if b is zero.")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Quotient of a divided by b"),
        @APIResponse(responseCode = "400", description = "Division by zero")
    })
    public Response divide(
            @Parameter(description = "Dividend", required = true) @QueryParam("a") double a,
            @Parameter(description = "Divisor (must not be zero)", required = true) @QueryParam("b") double b) {
        try {
            return Response.ok(new CalculationResult(calculatorService.divide(a, b))).build();
        } catch (ArithmeticException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    public record CalculationResult(double result) {}
}
