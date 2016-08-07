package com.nelkinda.training.java8.exercise4;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collection;

import static java.lang.System.setErr;
import static java.lang.System.setIn;
import static java.lang.System.setOut;
import static java.lang.System.setProperty;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class SortByLengthTest {
    private static String oldLineSeparator;
    private final InputStream oldStdin = System.in;
    private final ByteArrayInputStream in;
    private final PrintStream oldStdout = System.out;
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final PrintStream oldStderr = System.err;
    private final ByteArrayOutputStream err = new ByteArrayOutputStream();
    private final String expectedOutput;

    public SortByLengthTest(final String input, final String expectedOutput) {
        in = new ByteArrayInputStream(input.getBytes(Charset.forName("UTF-8")));
        this.expectedOutput = expectedOutput;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return asList(new Object[][] {
                { "", "" },
                { "foo\n", "foo\n" },
                { "foo\nf\nfo\n", "f\nfo\nfoo\n"},
                { "bar\nfoo\nb\nf\n", "b\nf\nbar\nfoo\n"}
        });
    }

    @BeforeClass
    public static void overrideLineSeparator() {
        oldLineSeparator = setProperty("line.separator", "\n");
    }

    @AfterClass
    public static void restoreLineSeparator() {
        setProperty("line.separator", oldLineSeparator);
    }

    @Before
    public void redirectStdStreams() {
        setIn(in);
        setOut(new PrintStream(out));
        setErr(new PrintStream(err));
    }

    @After
    public void stdoutContainsExpectedOutput() throws UnsupportedEncodingException {
        assertEquals(expectedOutput, out.toString("UTF-8"));
    }

    @After
    public void stderrIsEmpty() throws UnsupportedEncodingException {
        assertEquals("", err.toString("UTF-8"));
    }

    @After
    public void restoreStdStreams() {
        setIn(oldStdin);
        setOut(oldStdout);
        setErr(oldStderr);
    }

    @Test
    public void sortNothing() throws IOException {
        SortByLength.main();
    }
}
