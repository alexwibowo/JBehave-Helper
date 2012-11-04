JBehave Helper
==============

A collection of helper classes / methods for writing [JBehave](http://www.jbehave.org) tests.


Spring Integration
------------------

### The current situation ..
JBehave-spring project gives you **SpringStepsFactory** class, which allows the *step* classes
to be managed inside the Spring container. Here is a snippet from JBehave documentation:

```
 @Override
 public InjectableStepsFactory stepsFactory() {
    return new SpringStepsFactory(configuration(), createContext());
 }

 private ApplicationContext createContext() {
    return new SpringApplicationContextFactory("org/jbehave/examples/trader/spring/steps.xml").createApplicationContext();
 }
```

### The problem - more like.. *what troubles me*
which is probably ok for some of you, but:

1. I personally think that there is a bit of boilerplate code.

2. I was a bit lost when reading the snippet above.

   *"Where should I put these methods? They must be overridden from an abstract class, or implemented from some interface."* The later is not too bad, but the former might not be desirable to some of you.

### The solution

First, we want to stay away from enforcing our test class structure to inherit from JBehave class (or implement their interface).

*Ideally* we would like to stay close to JUnit test class style. That is, to write a test, we should not need to tie our test class inheritance structure
to any class. However, given that we want to integrate JBehave with our Spring based project, it is probably ok if we extend test classes from Spring. Namely, *AbstractJUnit4SpringContextTests* and *AbstractTransactionalJUnit4SpringContextTests*. So this is the approach that we are taking.
 
### SpringBasedStory

Using SpringBasedStory, you will need to do the following:

1. Write a test class that extends *SpringBasedStory*, and override the *getStoryPaths* method. This method should return the path to your JBehave story files.
   Note that *SpringBasedStory* class extends AbstractJUnit4SpringContextTests from Spring.

```
@ContextConfiguration("classpath:spring/springContext-jbehave.xml")
public class NumberStory extends SpringBasedStory {

  protected List<String> getStoryPaths() {
     return asList("org/isolution/jbehave/addition.story","org/isolution/jbehave/subtraction.story");
  }
}
```

2. Write your JBehave story as usual.

3. Write the step class, and perhaps annotate it with @Component to be registered in Spring container.

```
  @Component
  public class SampleSteps {
      @Given("I have number '$number'")
      public void givenIHaveNumber(long number) {
          ...
      }
      ...
  }
```

