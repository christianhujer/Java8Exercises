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
import java.awt.event.ActionEvent;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

import static javax.swing.SwingUtilities.invokeAndWait;
import static javax.swing.SwingUtilities.invokeLater;
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
    public void iHaveJustStartedTheEditor() throws InvocationTargetException, InterruptedException {
        invokeAndWait(new Runnable() {
            @Override public void run() {
                editor = new Editor();
                editorComponent = findComponent(editor.getWindow(), JTextComponent.class);
            }
        });
        assertNotNull(editorComponent);
    }

    @When("^I enter the text \"([^\"]*)\"[,.]?$")
    public void iEnterTheText(final String text) throws BadLocationException {
        final Document document = editorComponent.getDocument();
        document.insertString(document.getLength(), text, null);
    }

    @When("^I action \"([^\"]*)\"[,.]?$")
    public void iAction(final String actionCommand) {
        invokeLater(new Runnable() {
            @Override public void run() {
                editor.getActions().get(actionCommand)
                        .actionPerformed(new ActionEvent(editorComponent, 0, actionCommand));
            }
        });
    }

    @When("^I wait for action \"([^\"]*)\"[,.]?$")
    public void iWaitForAction(final String actionCommand) throws InvocationTargetException, InterruptedException {
        invokeAndWait(new Runnable() {
            @Override public void run() {
                editor.getActions().get(actionCommand)
                        .actionPerformed(new ActionEvent(editorComponent, 0, actionCommand));
            }
        });
    }

    @When("^I enter the filename \"([^\"]*)\"[,.]?$")
    public void iEnterTheFilename(final String filename) throws Throwable {
        final File file = new File(filename);
        invokeAndWait(new Runnable() {
            @Override public void run() {
                editor.fileChooser.setSelectedFile(file);
                editor.fileChooser.approveSelection();
            }
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

    @Then("^I must be asked for a filename[,.]?$")
    public void iAmAskedForAFilename() throws Throwable {
        final boolean[] isShowing = new boolean[1];
        invokeAndWait(new Runnable() {
            @Override public void run() {
                isShowing[0] = editor.fileChooser.isShowing();
            }
        });
        assertTrue(isShowing[0]);
    }

    @Then("^I must not be asked for a filename[,.]?$")
    public void iAmNotAskedForAFilename() throws Throwable {
        assertFalse(editor.fileChooser.isShowing());
    }

    @When("^I set the caret to position (\\d+)[,.]?$")
    public void iSetTheCursorToPosition(final int caretPosition) throws Throwable {
        editorComponent.setCaretPosition(caretPosition);
    }

    @When("^I mark from position (\\d+) to position (\\d+),$")
    public void iMarkFromPositionToPosition(final int start, final int end) throws Throwable {
        editorComponent.select(start, end);
    }

    @After
    public void closeTheEditor() {
        editor.quit(null);
    }
}
