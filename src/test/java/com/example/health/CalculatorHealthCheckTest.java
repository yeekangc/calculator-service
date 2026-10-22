package com.example.health;

import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.Readiness;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CalculatorHealthCheckTest {

    @Test
    public void livenessCheck_isAnnotatedWithLiveness() {
        assertNotNull(CalculatorLivenessCheck.class.getAnnotation(Liveness.class),
                "CalculatorLivenessCheck must be annotated with @Liveness");
    }

    @Test
    public void livenessCheck_implementsHealthCheck() {
        CalculatorLivenessCheck check = new CalculatorLivenessCheck();
        assertTrue(check instanceof org.eclipse.microprofile.health.HealthCheck);
    }

    @Test
    public void readinessCheck_isAnnotatedWithReadiness() {
        assertNotNull(CalculatorReadinessCheck.class.getAnnotation(Readiness.class),
                "CalculatorReadinessCheck must be annotated with @Readiness");
    }

    @Test
    public void readinessCheck_implementsHealthCheck() {
        CalculatorReadinessCheck check = new CalculatorReadinessCheck();
        assertTrue(check instanceof org.eclipse.microprofile.health.HealthCheck);
    }
}
