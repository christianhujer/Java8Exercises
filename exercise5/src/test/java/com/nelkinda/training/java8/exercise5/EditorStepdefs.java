package com.nelkinda.training.java8.exercise5;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class EditorStepdefs {
    private Editor editor;
    private JTextComponent editorComponent;
    private Document document;

    private static <T extends Component> T findComponent(final Container container, final Class<T> componentClass) {
        for (final Component c : container.getComponents()) {
            if (componentClass.isInstance(c)) return componentClass.cast(c);
            if (c instanceof Container) {
                final T component = findComponent((Container) c, componentClass);
                if (component != null) return component;
            }
        }
        return null;
    }

    @Given("^I have just started the editor[,.]?$")
    public void iHaveJustStartedTheEditor() throws Throwable {
        editor = new Editor();
        editorComponent = findComponent(editor.getWindow(), JTextComponent.class);
        assertNotNull(editorComponent);
        document = editorComponent.getDocument();
    }

    @Then("^the document name is \"([^\"]*)\"[,.]?$")
    public void theDocumentNameIs(final String expectedDocumentName) throws Throwable {
        assertEquals(expectedDocumentName, editor.getDocumentName());
    }

    @Then("^the document is \"([^\"]*)\"[,.]?$")
    public void theDocumentIs(final String expectedDocumentText) throws Throwable {
        assertEquals(expectedDocumentText, editorComponent.getText());
    }

    @When("^I enter the text \"([^\"]*)\"[,.]?$")
    public void iEnterTheText(final String text) throws Throwable {
        document.insertString(document.getLength(), text, null);
    }

    @Then("^the window title is \"([^\"]*)\"[,.]?$")
    public void theWindowTitleIs(final String expectedWindowTitle) throws Throwable {
        assertEquals(expectedWindowTitle, editor.getWindow().getTitle());
    }

    @When("^I action \"([^\"]*)\"[,.]?$")
    public void iAction(final String actionCommand) throws Throwable {
        editor.getActions().get(actionCommand).actionPerformed(null);
    }

    @And("^a file \"([^\"]*)\" does not exist[,.]?$")
    public void aFileDoesNotExist(final String filename) throws Throwable {
        final File file = new File(filename);
        if (file.exists())
            assertTrue(file.delete());
    }
}
