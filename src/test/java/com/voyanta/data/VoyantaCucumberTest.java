package com.voyanta.data;

import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@Cucumber.Options(tags="@done",format = {"pretty", "html:target/cucumber","json:target/cucumber.json"})
public class VoyantaCucumberTest {
}





