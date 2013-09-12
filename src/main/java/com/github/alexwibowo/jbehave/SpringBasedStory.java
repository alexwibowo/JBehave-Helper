package com.github.alexwibowo.jbehave;

import org.jbehave.core.configuration.AnnotationBuilder;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.configuration.spring.SpringStoryControls;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.failures.FailingUponPendingStep;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryLoader;
import org.jbehave.core.reporters.CrossReference;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.DelegatingStepMonitor;
import org.jbehave.core.steps.spring.SpringStepsFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.util.List;

/**
 * Base test class for running JBehave test.
 * <p/>
 * User: alexwibowo
 * Date: 24/10/12
 * Time: 9:58 PM
 */
public abstract class SpringBasedStory extends AbstractJUnit4SpringContextTests{

    private Embedder embedder;

    @Autowired
    private ApplicationContext applicationContext;

    private final CrossReference crossReference;

    private final StoryReporterBuilder storyReportBuilder;

    public SpringBasedStory() {
        storyReportBuilder = getStoryReportBuilder();
        crossReference = getCrossReference();
    }

    @Before
    public void setup(){
        embedder = new AnnotationBuilder(this.getClass()).buildEmbedder();

        SpringStepsFactory stepsFactory = new SpringStepsFactory(embedder.configuration(), applicationContext);
        embedder.useStepsFactory(stepsFactory);
        embedder.embedderControls()
                .doIgnoreFailureInStories(false)
                .doIgnoreFailureInView(false)
                .doGenerateViewAfterStories(true);

        SpringStoryControls storyControls = new SpringStoryControls();
        storyControls.doResetStateBeforeScenario(true)
                    .doResetStateBeforeStory(true);

        Configuration configuration = new MostUsefulConfiguration();
        configuration.useStoryLoader(getStoryLoader())
                .useStoryReporterBuilder(storyReportBuilder)
                .useStoryControls(storyControls)
                .useStepMonitor(new DelegatingStepMonitor(crossReference.getStepMonitor()));

        embedder.useConfiguration(configuration);
    }

    protected Embedder getEmbedder(){
        return embedder;
    }

    protected  StoryLoader getStoryLoader(){
        return new LoadFromClasspath(this.getClass());
    }

    protected StoryReporterBuilder getStoryReportBuilder() {
        return new StoryReporterBuilder()
                .withDefaultFormats()
                .withFormats(getReportFormats())
                .withFailureTrace(true)
                .withFailureTraceCompression(true)
                .withCrossReference(getCrossReference());
    }

    protected CrossReference getCrossReference() {
        return new CrossReference()
                .withJsonOnly()
                .withOutputAfterEachStory(true)
                .withPendingStepStrategy(new FailingUponPendingStep());
    }

    /**
     * Override with the desired report formats.
     *
     * @return array of {@link Format} report format
     */
    protected Format[] getReportFormats() {
        return new Format[]{Format.CONSOLE, Format.HTML};
    }

    @Test
    public void test() {
        List<String> storyPaths = getStoryPaths();
        try {
            embedder.runStoriesAsPaths(storyPaths);
        } finally {
            embedder.generateCrossReference();
        }

    }

    /**
     * @return List of paths for the story files to be loaded
     */
    protected abstract List<String> getStoryPaths();
}
