package com.grmek.rso.imagemanaging;

import javax.enterprise.context.ApplicationScoped;
import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

@ApplicationScoped
@ConfigBundle("configuration-properties")
public class ConfigurationProperties {

    @ConfigValue(value = "users-parameter", watch = true)
    private String usersParameter;

    public String getUsersParameter() {
        return usersParameter;
    }

    public void setUsersParameter(String usersParameter) {
        this.usersParameter = usersParameter;
    }
}
