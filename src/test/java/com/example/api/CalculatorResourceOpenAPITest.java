package com.example.api;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorResourceOpenAPITest {

    @Test
    public void calculatorResource_hasTagAnnotation() {
        Tag tag = CalculatorResource.class.getAnnotation(Tag.class);
        assertNotNull(tag, "CalculatorResource must have @Tag annotation");
        assertEquals("Calculator", tag.name());
    }

    @Test
    public void addMethod_hasOperationAnnotation() throws NoSuchMethodException {
        Method method = CalculatorResource.class.getMethod("add", double.class, double.class);
        Operation op = method.getAnnotation(Operation.class);
        assertNotNull(op, "add() must have @Operation annotation");
        assertEquals("Add two numbers", op.summary());
    }

    @Test
    public void subtractMethod_hasOperationAnnotation() throws NoSuchMethodException {
        Method method = CalculatorResource.class.getMethod("subtract", double.class, double.class);
        Operation op = method.getAnnotation(Operation.class);
        assertNotNull(op, "subtract() must have @Operation annotation");
        assertEquals("Subtract two numbers", op.summary());
    }

    @Test
    public void multiplyMethod_hasOperationAnnotation() throws NoSuchMethodException {
        Method method = CalculatorResource.class.getMethod("multiply", double.class, double.class);
        Operation op = method.getAnnotation(Operation.class);
        assertNotNull(op, "multiply() must have @Operation annotation");
        assertEquals("Multiply two numbers", op.summary());
    }

    @Test
    public void divideMethod_hasOperationAnnotation() throws NoSuchMethodException {
        Method method = CalculatorResource.class.getMethod("divide", double.class, double.class);
        Operation op = method.getAnnotation(Operation.class);
        assertNotNull(op, "divide() must have @Operation annotation");
        assertEquals("Divide two numbers", op.summary());
    }
}
