package org.isolution.jbehave.selenium;

import org.isolution.jbehave.BaseSeleniumJBehaveSpringConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(BaseSeleniumJBehaveSpringConfig.class)
@ComponentScan(
        basePackages = "org.isolution.jbehave.selenium.steps"
)
public class SeleniumStorySpringConfig {
}
