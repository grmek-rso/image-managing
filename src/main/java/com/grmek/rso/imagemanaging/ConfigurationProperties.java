package com.grmek.rso.imagemanaging;

import javax.enterprise.context.ApplicationScoped;
import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

@ApplicationScoped
@ConfigBundle("configuration-properties")
public class ConfigurationProperties {

    @ConfigValue(value = "demo-etcd-parameter", watch = true)
    private String demoEtcdParameter;

    public String getDemoEtcdParameter() {
        return demoEtcdParameter;
    }

    public void setDemoEtcdParameter(String demoEtcdParameter) {
        this.demoEtcdParameter = demoEtcdParameter;
    }

    @ConfigValue(value = "demo-service-is-healthy", watch = true)
    private String demoServiceIsHealthy;

    public String getDemoServiceIsHealthy() {
        return demoServiceIsHealthy;
    }

    public void setDemoServiceIsHealthy(String demoServiceIsHealthy) {
        this.demoServiceIsHealthy = demoServiceIsHealthy;
    }
}
