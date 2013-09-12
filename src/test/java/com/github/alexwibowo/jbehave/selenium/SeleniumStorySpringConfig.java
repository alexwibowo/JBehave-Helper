package com.github.alexwibowo.jbehave.selenium;

import com.github.alexwibowo.jbehave.web.selenium.BaseSeleniumJBehaveSpringConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(BaseSeleniumJBehaveSpringConfig.class)
@ComponentScan(
        basePackages = "com.github.alexwibowo.jbehave.selenium.steps"
)
public class SeleniumStorySpringConfig {
}
