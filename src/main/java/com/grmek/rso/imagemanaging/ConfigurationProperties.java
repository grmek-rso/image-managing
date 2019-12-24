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

    @ConfigValue(value = "db-url", watch = true)
    private String dbUrl;

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    @ConfigValue(value = "db-user", watch = true)
    private String dbUser;

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    @ConfigValue(value = "db-password", watch = true)
    private String dbPassword;

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }
}
