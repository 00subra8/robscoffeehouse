package features;

import cucumber.api.java.en.When;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CucumberSteps {

    @When("^I click this$")
    public void test() {
        System.out.print("Comes here");
    }
}
