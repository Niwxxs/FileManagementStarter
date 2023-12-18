package com.niwxxs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Niwxxs
 * @create 2023-12-14-下午3:28
 */
@ConfigurationProperties(prefix = "file")
public class InitConfig {

    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
