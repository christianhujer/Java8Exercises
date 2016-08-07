package com.nelkinda.training.java8.exercise1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class Counter {
    private final JFrame frame;
    private int counter;

    Counter() {
        frame = new JFrame("Counter");
        frame.setName(getComponentName("Frame"));
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        final AbstractButton button = new JButton("Click me!");
        button.setName(getComponentName("Button"));
        button.setActionCommand("click");

        final JLabel label = new JLabel("You've clicked me 0 times.     ");
        label.setName(getComponentName("label"));

        // TODO Replace anonymous class with lambda.
        button.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        counter++;
                        label.setText("You've clicked the button " + counter + " times.");
                    }
                }
        );

        final Container contentPane = frame.getContentPane();
        contentPane.setLayout(new GridLayout(2, 1));
        contentPane.add(label);
        contentPane.add(button);
        frame.pack();
    }

    public static void main(final String... args) {
        final Counter counter = new Counter();
        counter.show();
    }

    private void show() {
        frame.setVisible(true);
    }

    int getCounter() {
        return counter;
    }

    String getComponentName(final String name) {
        return getClass().getPackage().getName() + "." + name;
    }
}
