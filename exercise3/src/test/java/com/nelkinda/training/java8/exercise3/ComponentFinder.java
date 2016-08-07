package com.nelkinda.training.java8.exercise3;

import java.awt.*;

import static java.awt.Window.getOwnerlessWindows;

enum ComponentFinder {
    ;

    private static Container ownerlessWindowsContainer = new Container() {
        @Override
        public Component[] getComponents() {
            return getOwnerlessWindows();
        }
    };

    public static Component findComponent(final String name) {
        return findComponent(ownerlessWindowsContainer, name);
    }

    public static Component findComponent(final Container container, final String name) {
        for (final Component c : container.getComponents()) {
            if (name.equals(c.getName())) return c;
            if (c instanceof Container) {
                final Component component = findComponent((Container) c, name);
                if (component != null) return component;
            }
        }
        return null;
    }
}
