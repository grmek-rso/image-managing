package com.grmek.rso.imagemanaging;

import javax.enterprise.context.ApplicationScoped;
import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

@ApplicationScoped
@ConfigBundle("configuration-properties")
public class ConfigurationProperties {

    @ConfigValue(value = "customers-parameter", watch = true)
    private String customersParameter;

    public String getCustomersParameter() {
        return customersParameter;
    }

    public void setCustomersParameter(String customersParameter) {
        this.customersParameter = customersParameter;
    }

}
