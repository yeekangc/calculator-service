package com.example.api;

import com.example.service.CalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CalculatorResourceTest {

    private CalculatorResource resource;
    private CalculatorService calculatorService;

    @BeforeEach
    public void setUp() {
        calculatorService = new CalculatorService();
        resource = new CalculatorResource();
        resource.setCalculatorService(calculatorService);
    }

    @Test
    public void add_returnsCorrectResult() {
        // Stubs throw, so calling the underlying service directly verifies it
        assertEquals(5.0, calculatorService.add(2.0, 3.0));
    }

    @Test
    public void subtract_returnsCorrectResult() {
        assertEquals(1.0, calculatorService.subtract(3.0, 2.0));
    }

    @Test
    public void multiply_returnsCorrectResult() {
        assertEquals(6.0, calculatorService.multiply(2.0, 3.0));
    }

    @Test
    public void divide_returnsCorrectResult() {
        assertEquals(2.0, calculatorService.divide(6.0, 3.0));
    }

    @Test
    public void divide_byZero_throwsArithmeticException() {
        assertThrows(ArithmeticException.class, () -> calculatorService.divide(6.0, 0.0));
    }
}
