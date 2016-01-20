package net.devromik.app.springAppConfig;

import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan(basePackages = {"net.devromik.app"})
@PropertySource("classpath:/appInfo.properties")
@EnableMBeanExport
public class AppConfig {

    public static final String BUILD_NUMBER_JVM_PROPERTY_NAME = "buildNumber";

    // ****************************** //

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}