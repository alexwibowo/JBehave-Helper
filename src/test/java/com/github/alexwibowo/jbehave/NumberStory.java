package com.github.alexwibowo.jbehave;

import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * User: alexwibowo
 * Date: 24/10/12
 * Time: 8:58 PM
 */
@ContextConfiguration("classpath:spring/springContext-jbehave.xml")
public class NumberStory extends SpringBasedStory {

    protected List<String> getStoryPaths() {
//        StoryPathResolver pathResolver = getEmbedder().configuration().storyPathResolver();
//        String storyPath = pathResolver.resolve((Class<? extends Embeddable>) this.getClass());
        return asList("com/github/alexwibowo/jbehave/addition.story",
                "com/github/alexwibowo/jbehave/subtraction.story");
    }
}
