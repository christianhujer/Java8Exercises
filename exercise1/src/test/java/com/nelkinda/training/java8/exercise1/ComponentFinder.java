package com.nelkinda.training.java8.exercise1;

import java.awt.*;

enum ComponentFinder {;

    private static Container ownerlessWindowsContainer = new Container() {
        @Override
        public Component[] getComponents() {
            return Window.getOwnerlessWindows();
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