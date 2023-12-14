package com.niwxxs.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Niwxxs
 * @create 2023-12-14-下午3:39
 */
@Configuration
@EnableConfigurationProperties(InitConfig.class)
public class InitAutoConfiguration {

    @Bean
    public InitConfig initConfig(InitConfig initConfig){
        return new InitConfig();
    }

}
