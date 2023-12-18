package com.niwxxs.config;

import com.niwxxs.modules.FileManagementModule;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @author Niwxxs
 * @create 2023-12-14-下午3:39
 */
@Configuration
@EnableConfigurationProperties(InitConfig.class)
public class InitAutoConfiguration {

    @Bean
    public FileManagementModule initConfig(InitConfig initConfig){
        final FileManagementModule fileManagementModule = new FileManagementModule();
        final String path = StringUtils.isEmpty(initConfig.getPath()) ? System.getProperty("user.dir") : initConfig.getPath();
        fileManagementModule.setPath(path);
        return fileManagementModule;
    }

}
