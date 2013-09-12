package com.github.alexwibowo.jbehave.selenium;

import com.github.alexwibowo.jbehave.web.selenium.SeleniumSpringBasedStory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.List;

import static java.util.Arrays.asList;

@ContextConfiguration(
        loader = AnnotationConfigContextLoader.class,
        classes = SeleniumStorySpringConfig.class
)
public class GoogleStory extends SeleniumSpringBasedStory {

    @Override
    protected List<String> getStoryPaths() {
        return asList("com/github/alexwibowo/jbehave/selenium/google.story");
    }
}
