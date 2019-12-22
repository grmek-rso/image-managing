package com.grmek.rso.imagemanaging;

import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Liveness
@ApplicationScoped
public class DemoHealthCheck implements HealthCheck {

    @Inject
    private ConfigurationProperties configurationProperties;

    public HealthCheckResponse call() {
        if (configurationProperties.getDemoServiceIsHealthy().equals("true")) {
            return HealthCheckResponse.up(DemoHealthCheck.class.getSimpleName());
        }
        else {
            return HealthCheckResponse.down(DemoHealthCheck.class.getSimpleName());
        }
    }
}
