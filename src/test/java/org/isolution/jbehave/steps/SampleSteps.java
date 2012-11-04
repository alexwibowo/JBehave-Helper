package org.isolution.jbehave.steps;

import org.hamcrest.Matchers;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * User: alexwibowo
 * Date: 24/10/12
 * Time: 8:58 PM
 */
@Component
public class SampleSteps {

    @Autowired
    private ResultHolder resultHolder;

    @Given("I have number '$number'")
    public void givenIHaveNumber(long number) {
        resultHolder.set(new AtomicLong(number));

    }

    @When("I add '$number' to it")
    public void whenIAdd(long number) {
        AtomicLong currentNumber = resultHolder.get();
        long result = currentNumber.addAndGet(number);
        resultHolder.set(new AtomicLong(result));
    }

    @When("I subtract '$number' from it")
    public void whenISubtract(long number){
        AtomicLong currentNumber = resultHolder.get();
        long result = currentNumber.addAndGet((-1) * number);
        resultHolder.set(new AtomicLong(result));
    }

    @Then("I should get '$number'")
    public void iShouldGet(long number) {
        AtomicLong currentNumber = resultHolder.get();
        assertThat(currentNumber.get(), equalTo(number));
    }

}
