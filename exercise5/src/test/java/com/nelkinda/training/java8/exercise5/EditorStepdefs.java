package com.nelkinda.training.java8.exercise5;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class EditorStepdefs {
    private Editor editor;
    private JTextComponent editorComponent;

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
    public void iHaveJustStartedTheEditor() {
        editor = new Editor();
        editorComponent = findComponent(editor.getWindow(), JTextComponent.class);
        assertNotNull(editorComponent);
    }

    @Given("^the file \"([^\"]*)\" has the following content:$")
    public void theFileHasTheFollowingContent(final String filename, final String content) throws IOException {
        Files.write(Paths.get(filename), content.getBytes("UTF-8"));
    }

    @Given("^the file \"([^\"]*)\" does not exist[,.]?$")
    public void theFileDoesNotExist(final String filename) {
        final File file = new File(filename);
        if (file.exists())
            assertTrue(file.delete());
    }

    @When("^I enter the text \"([^\"]*)\"[,.]?$")
    public void iEnterTheText(final String text) throws BadLocationException {
        final Document document = editorComponent.getDocument();
        document.insertString(document.getLength(), text, null);
    }

    @When("^I action \"([^\"]*)\"[,.]?$")
    public void iAction(final String actionCommand) {
        SwingUtilities.invokeLater(() -> editor.getActions().get(actionCommand).actionPerformed(null));
    }

    @When("^I wait for action \"([^\"]*)\"[,.]?$")
    public void iWaitForAction(final String actionCommand) throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> editor.getActions().get(actionCommand).actionPerformed(null));
    }

    @When("^I enter the filename \"([^\"]*)\"[,.]?$")
    public void iEnterTheFilename(final String filename) throws Throwable {
        final File file = new File(filename);
        SwingUtilities.invokeAndWait(() -> {
            editor.fileChooser.setSelectedFile(file);
            editor.fileChooser.approveSelection();
        });
    }

    @When("^I wait for I/O to be completed[,.]$")
    public void iWaitForIOToBeCompleted() throws Throwable {
        final SwingWorker lastWorker = editor.getLastWorker();
        lastWorker.get();
        lastWorker.get();
    }

    @Then("^the document name must be \"([^\"]*)\"[,.]?$")
    public void theDocumentNameMustBe(final String expectedDocumentName) throws InterruptedException {
        assertEquals(expectedDocumentName, editor.getDocumentName());
    }

    @Then("^the window title must be \"([^\"]*)\"[,.]?$")
    public void theWindowTitleMustBe(final String expectedWindowTitle) {
        assertEquals(expectedWindowTitle, editor.getWindow().getTitle());
    }

    @Then("^the document must have the following content:$")
    public void theDocumentMustHaveTheFollowingContent(final String expectedDocumentText) {
        assertEquals(expectedDocumentText, editorComponent.getText());
    }

    @Then("^the file \"([^\"]*)\" must have the following content:$")
    public void theFileMustHaveTheFollowingContent(final String filename, final String expectedContent)
            throws IOException {
        final String actualContent = new String(Files.readAllBytes(Paths.get(filename)), "UTF-8");
        assertEquals(expectedContent, actualContent);
    }

    @Then("^I must be asked for a filename[,.]?$")
    public void iAmAskedForAFilename() throws Throwable {
        Thread.sleep(100);
        assertTrue(editor.fileChooser.isShowing());
    }

    @Then("^I must not be asked for a filename[,.]?$")
    public void iAmNotAskedForAFilename() throws Throwable {
        assertFalse(editor.fileChooser.isShowing());
    }

    @After
    public void closeTheEditor() {
        editor.quit(null);
    }
}
