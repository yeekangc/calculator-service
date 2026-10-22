package com.example.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CalculatorServiceTest {

    private CalculatorService calculator;

    @BeforeEach
    public void setUp() {
        calculator = new CalculatorService();
    }

    @Test
    public void add_returnsSumOfTwoNumbers() {
        assertEquals(5.0, calculator.add(2.0, 3.0));
    }

    @Test
    public void subtract_returnsDifferenceOfTwoNumbers() {
        assertEquals(1.0, calculator.subtract(3.0, 2.0));
    }

    @Test
    public void multiply_returnsProductOfTwoNumbers() {
        assertEquals(6.0, calculator.multiply(2.0, 3.0));
    }

    @Test
    public void divide_returnsQuotientOfTwoNumbers() {
        assertEquals(2.0, calculator.divide(6.0, 3.0));
    }

    @Test
    public void divide_byZero_throwsArithmeticException() {
        assertThrows(ArithmeticException.class, () -> calculator.divide(6.0, 0.0));
    }
}
