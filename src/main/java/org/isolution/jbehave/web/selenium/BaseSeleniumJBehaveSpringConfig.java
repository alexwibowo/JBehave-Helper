package org.isolution.jbehave.web.selenium;

import org.isolution.jbehave.web.selenium.LocalWebDriverProvider;
import org.isolution.jbehave.web.selenium.WebDriverHtmlWithScreenshotOutput;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.spring.SpringStoryControls;
import org.jbehave.core.configuration.spring.SpringStoryReporterBuilder;
import org.jbehave.core.embedder.*;
import org.jbehave.core.failures.FailingUponPendingStep;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.model.ExamplesTableFactory;
import org.jbehave.core.parsers.RegexStoryParser;
import org.jbehave.core.parsers.StoryParser;
import org.jbehave.core.reporters.*;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.StepMonitor;
import org.jbehave.core.steps.spring.SpringStepsFactory;
import org.jbehave.web.selenium.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.jbehave.core.reporters.Format.CONSOLE;

@org.springframework.context.annotation.Configuration
public class BaseSeleniumJBehaveSpringConfig {

    @Autowired
    protected ApplicationContext context;

    @Bean
    public WebDriverProvider webDriverProvider() {
        return new LocalWebDriverProvider();
    }

    @Bean
    public SeleniumContext seleniumContext() {
        return new SeleniumContext();
    }

    @Bean
    public StepMonitor seleniumStepMonitor() {
        ContextView contextView = new LocalFrameContextView().sized(640, 120);
        return new SeleniumStepMonitor(contextView, seleniumContext(), crossReference().getStepMonitor());
    }

    @Bean
    public StoryReporterBuilder reporterBuilder() {
        final WebDriverProvider webDriverProvider = webDriverProvider();
        Format HTML_WITH_SCREENSHOTS = new Format("HTML") {
            @Override
            public StoryReporter createStoryReporter(FilePrintStreamFactory factory,
                                                     StoryReporterBuilder storyReporterBuilder) {
                factory.useConfiguration(storyReporterBuilder.fileConfiguration("html"));
                return new WebDriverHtmlWithScreenshotOutput(factory.createPrintStream(), storyReporterBuilder, webDriverProvider)
                        .doReportFailureTrace(storyReporterBuilder.reportFailureTrace())
                        .doCompressFailureTrace(storyReporterBuilder.compressFailureTrace());
            }
        };

        Format[] formats = new Format[]{
                new SeleniumContextOutput(seleniumContext()),
                CONSOLE,
                HTML_WITH_SCREENSHOTS
        };
        return new SpringStoryReporterBuilder()
                .withFailureTrace(true)
                .withFailureTraceCompression(true)
                .withDefaultFormats()
                .withCrossReference(crossReference())
                .withFormats(formats);
    }

    @Bean
    public Configuration configuration() {
        return new SeleniumConfiguration().useSeleniumContext(seleniumContext())
                .useStoryControls(storyControls())
                .useStoryParser(storyParser())
                .usePendingStepStrategy(getPendingStepStrategy())
                .useStepMonitor(seleniumStepMonitor())
                .useStoryReporterBuilder(reporterBuilder());
    }

    @Bean
    public StoryParser storyParser() {
        return new RegexStoryParser(
                    new ExamplesTableFactory(
                            new LoadFromClasspath(this.getClass())
                    )
            );
    }

    @Bean
    public StoryControls storyControls() {
        return new SpringStoryControls()
                    .doDryRun(false)
                    .doSkipScenariosAfterFailure(false)
                    .doResetStateBeforeScenario(false);
    }

    protected FailingUponPendingStep getPendingStepStrategy() {
        return new FailingUponPendingStep();
    }

    @Bean
    public CrossReference crossReference() {
        return new CrossReference()
                .withJsonOnly()
                .withPendingStepStrategy(getPendingStepStrategy())
                .withOutputAfterEachStory(true)
                .excludingStoriesWithNoExecutedScenarios(true);
    }

    @Bean
    public Embedder jbehaveEmbedder() {
        Embedder embedder = new Embedder(new StoryMapper(), new StoryRunner(), new SilentEmbedderMonitor(System.out));
        embedder.useConfiguration(configuration());
        embedder.useStepsFactory(stepsFactory());
        embedder.embedderControls()
                .doIgnoreFailureInView(false)
                .doIgnoreFailureInStories(false)
                .doVerboseFailures(true)
                .useThreads(1);
        embedder.embedderControls().useStoryTimeoutInSecs(getStoryTimeoutInSecs());
        return embedder;
    }

    protected int getStoryTimeoutInSecs() {
        return 10000;
    }

    @Bean
    public InjectableStepsFactory stepsFactory() {
        return new SpringStepsFactory(configuration(), context);
    }
}
