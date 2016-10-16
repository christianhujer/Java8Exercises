package com.nelkinda.training.java8.exercise5;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import static java.awt.Toolkit.getDefaultToolkit;
import static java.awt.datatransfer.DataFlavor.stringFlavor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SystemClipboardStepdefs {
    @Given("^the system clipboard is empty[,.]?$")
    public void theSystemClipboardIsEmpty() throws Throwable {
        theSystemClipboardContains("");
    }

    @Given("^the system clipboard contains \"([^\"]*)\"[,.]?$")
    public void theSystemClipboardContains(final String clipboardText) throws Throwable {
        final Clipboard clipboard = getDefaultToolkit().getSystemClipboard();
        final StringSelection stringSelection = new StringSelection(clipboardText);
        attemptSetClipboardContents(clipboard, stringSelection);
    }

    private static void attemptSetClipboardContents(final Clipboard clipboard, final StringSelection stringSelection) throws InterruptedException {
        for (int count = 0; count < 3; count++)
            try {
                setClipboardContent(clipboard, stringSelection);
                return;
            } catch (final IllegalStateException ignore) {
                Thread.sleep(10);
            }
        setClipboardContent(clipboard, stringSelection);
    }

    private static void setClipboardContent(final Clipboard clipboard, final StringSelection stringSelection) {
        clipboard.setContents(stringSelection, stringSelection);
    }

    @Then("^the system clipboard must contain the text \"([^\"]*)\"[,.]?$")
    public void theSystemClipboardMustContainTheText(final String expectedClipboardContent) throws Throwable {
        final Clipboard clipboard = getDefaultToolkit().getSystemClipboard();
        final Transferable transferable = clipboard.getContents(null);
        assertNotNull(transferable);
        final String contents = (String) transferable.getTransferData(stringFlavor);
        assertEquals(expectedClipboardContent, contents);
    }

}
