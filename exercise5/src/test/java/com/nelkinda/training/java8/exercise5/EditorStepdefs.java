package com.nelkinda.training.java8.exercise5;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.SwingWorker;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

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

    public static <T> T callAndWait(final Callable<T> callable) throws InvocationTargetException, InterruptedException, ExecutionException {
        final FutureTask<T> futureTask = new FutureTask<>(callable);
        invokeAndWait(futureTask);
        return futureTask.get();
    }

    @Given("^I have just started the editor[,.]?$")
    public void iHaveJustStartedTheEditor() throws InvocationTargetException, InterruptedException {
        // TODO Replace with lambda or method reference
        invokeAndWait(new Runnable() {
            @Override public void run() {
                editor = new Editor();
                editorComponent = findComponent(editor.getWindow(), JTextComponent.class);
            }
        });
        assertNotNull(editorComponent);
    }

    @When("^I enter the text \"([^\"]*)\"[,.]?$")
    public void iEnterTheText(final String text)
            throws BadLocationException, InvocationTargetException, InterruptedException {
        // TODO Replace with lambda or method reference
        invokeAndWait(new Runnable() {
            @Override public void run() {
                final Document document = editorComponent.getDocument();
                try {
                    document.insertString(editorComponent.getCaretPosition(), text, null);
                } catch (final BadLocationException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @When("^I action \"([^\"]*)\"[,.]?$")
    public void iAction(final String actionCommand) throws InvocationTargetException, InterruptedException {
        // TODO Replace with lambda or method reference
        invokeLater(new Runnable() {
            @Override public void run() {
                final ActionMap actions = editor.getActions();
                final Action action = actions.get(actionCommand);
                assertNotNull(action);
                action.actionPerformed(new ActionEvent(editorComponent, 0, actionCommand));
            }
        });
    }

    @When("^I wait for action \"([^\"]*)\"[,.]?$")
    public void iWaitForAction(final String actionCommand) throws InvocationTargetException, InterruptedException {
        // TODO Replace with lambda or method reference
        invokeAndWait(new Runnable() {
            @Override public void run() {
                final Action action = editor.getActions().get(actionCommand);
                assertNotNull(action);
                action.actionPerformed(new ActionEvent(editorComponent, 0, actionCommand));
            }
        });
    }

    @When("^I enter the filename \"([^\"]*)\"[,.]?$")
    public void iEnterTheFilename(final String filename) throws Throwable {
        final File file = new File(filename);
        // TODO Replace with lambda or method reference
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
    }

    @Then("^the editor has focus[,.]?$")
    public void theEditorHasFocus() throws Throwable {
        final Object monitor = new Object();
        final FocusAdapter focusAdapter = new FocusAdapter() {
            @Override public void focusGained(final FocusEvent e) {
                synchronized (monitor) {
                    monitor.notify();
                }
            }
        };
        // TODO Replace with lambda or method reference
        final Callable<Boolean> hasFocus = new Callable<Boolean>() {
            @Override public Boolean call() {
                return editorComponent.hasFocus();
            }
        };
        // TODO Replace with lambda or method reference
        final Runnable addFocusListener = new Runnable() {
            @Override public void run() {
                editorComponent.addFocusListener(focusAdapter);
            }
        };
        // TODO Replace with lambda or method reference
        final Runnable removeFocusListener = new Runnable() {
            @Override public void run() {
                editorComponent.removeFocusListener(focusAdapter);
            }
        };
        // TODO Replace with lambda or method reference
        invokeAndWait(addFocusListener);
        // TODO Replace with lambda or method reference
        if (!callAndWait(hasFocus)) {
            synchronized (monitor) {
                try {
                    monitor.wait();
                } catch (final InterruptedException ignored) {
                }
            }
        }
        // TODO Replace with lambda or method reference
        invokeAndWait(removeFocusListener);
        // TODO Replace with lambda or method reference
        assertTrue(callAndWait(hasFocus));
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
        assertTrue(isFileChooserShowing());
    }

    @Then("^I must not be asked for a filename[,.]?$")
    public void iAmNotAskedForAFilename() throws Throwable {
        assertFalse(isFileChooserShowing());
    }

    private boolean isFileChooserShowing() throws InterruptedException, InvocationTargetException, ExecutionException {
        // TODO Replace with lambda or method reference
        return callAndWait(new Callable<Boolean>() {
            @Override public Boolean call() {
                return editor.fileChooser.isShowing();
            }
        });
    }

    @When("^I set the caret to position (\\d+)[,.]?$")
    public void iSetTheCursorToPosition(final int caretPosition) throws Throwable {
        // TODO Replace with lambda or method reference
        invokeAndWait(new Runnable() {
            @Override public void run() {
                editorComponent.setCaretPosition(caretPosition);
            }
        });
    }

    @When("^I mark from position (\\d+) to position (\\d+),$")
    public void iMarkFromPositionToPosition(final int start, final int end) throws Throwable {
        // TODO Replace with lambda or method reference
        invokeAndWait(new Runnable() {
            @Override public void run() {
                editorComponent.select(start, end);
            }
        });
    }

    @After
    public void closeTheEditor() throws InvocationTargetException, InterruptedException {
        iWaitForAction("quit");
        editorComponent = null;
        editor = null;
    }
}
