package com.nelkinda.training.java8.exercise5;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

import java.io.File;
import java.io.IOException;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Files.write;
import static java.nio.file.Paths.get;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileStepdefs {
    @Given("^the file \"([^\"]*)\" has the following content:$")
    public void theFileHasTheFollowingContent(final String filename, final String content) throws IOException {
        write(get(filename), content.getBytes("UTF-8"));
    }

    @Given("^the file \"([^\"]*)\" does not exist[,.]?$")
    public void theFileDoesNotExist(final String filename) {
        final File file = new File(filename);
        if (file.exists())
            assertTrue(file.delete());
    }

    @Then("^the file \"([^\"]*)\" must have the following content:$")
    public void theFileMustHaveTheFollowingContent(final String filename, final String expectedContent)
            throws IOException {
        final String actualContent = new String(readAllBytes(get(filename)), "UTF-8");
        assertEquals(expectedContent, actualContent);
    }
}
