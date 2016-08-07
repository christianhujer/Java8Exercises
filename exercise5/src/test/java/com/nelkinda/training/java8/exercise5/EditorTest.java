package com.nelkinda.training.java8.exercise5;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@CucumberOptions(
        features = { "src/test/resources/features/" },
        glue = { "com.nelkinda.training.java8.exercise5" }
)
@RunWith(Cucumber.class)
public class EditorTest {
}
