package org.isolution.jbehave;

import org.jbehave.core.embedder.Embedder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.util.List;


public abstract class SeleniumSpringBasedStory extends AbstractJUnit4SpringContextTests {

    @Autowired
    private Embedder embedder;

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
