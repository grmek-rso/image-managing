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
    private ConfigurationProperties cfg;

    public HealthCheckResponse call() {
        if (cfg.getDemoServiceIsHealthy().equals("true")) {
            return HealthCheckResponse.up(DemoHealthCheck.class.getSimpleName());
        }
        else {
            return HealthCheckResponse.down(DemoHealthCheck.class.getSimpleName());
        }
    }
}
