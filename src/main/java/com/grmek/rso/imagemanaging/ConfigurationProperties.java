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

    @ConfigValue(value = "gcp-project", watch = true)
    private String gcpProject;

    public String getGcpProject() {
        return gcpProject;
    }

    public void setGcpProject(String gcpProject) {
        this.gcpProject = gcpProject;
    }

    @ConfigValue(value = "gcp-key", watch = true)
    private String gcpKey;

    public String getGcpKey() {
        return gcpKey;
    }

    public void setGcpKey(String gcpKey) {
        this.gcpKey = gcpKey;
    }

    @ConfigValue(value = "gcp-storage-bucket", watch = true)
    private String gcpStorageBucket;

    public String getGcpStorageBucket() {
        return gcpStorageBucket;
    }

    public void setGcpStorageBucket(String gcpStorageBucket) {
        this.gcpStorageBucket = gcpStorageBucket;
    }
}
