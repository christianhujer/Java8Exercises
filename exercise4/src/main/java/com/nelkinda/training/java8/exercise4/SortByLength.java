package com.nelkinda.training.java8.exercise4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.List;

import static java.util.Collections.sort;
import static java.util.stream.Collectors.toList;

public class SortByLength {
    public static void main(final String... args) throws IOException {
        try (final BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            final List<String> lines = readLines(in);
            // TODO Replace anonymous class with method reference.
            sort(lines, new Comparator<String>() {
                @Override public int compare(final String o1, final String o2) {
                    return compareByLength(o1, o2);
                }
            });
            for (final String line : lines)
                System.out.println(line);
        }
    }

    public static int compareByLength(final String o1, final String o2) {
        int comparisonResult = ((Integer) o1.length()).compareTo(o2.length());
        if (comparisonResult == 0)
            comparisonResult = o1.compareTo(o2);
        return comparisonResult;
    }

    private static List<String> readLines(final BufferedReader in) {
        return in.lines().collect(toList());
    }
}
