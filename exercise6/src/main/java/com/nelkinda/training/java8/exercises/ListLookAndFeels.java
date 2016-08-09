package com.nelkinda.training.java8.exercises;

import javax.swing.*;
import java.util.Arrays;
import java.util.Comparator;

public class ListLookAndFeels {
    public static void main(final String... args) {
        // TODO Replace with a Stream that is sorted and then terminates with a forEach.
        final UIManager.LookAndFeelInfo[] installedLookAndFeels = UIManager.getInstalledLookAndFeels();
        Arrays.sort(installedLookAndFeels, new Comparator<UIManager.LookAndFeelInfo>() {
            @Override public int compare(final UIManager.LookAndFeelInfo o1, final UIManager.LookAndFeelInfo o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        for (final UIManager.LookAndFeelInfo lookAndFeelInfo : installedLookAndFeels)
            System.out.println(lookAndFeelInfo.getName());
    }
}
