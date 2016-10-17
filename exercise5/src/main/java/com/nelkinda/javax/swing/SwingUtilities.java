package com.nelkinda.javax.swing;

import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.function.Function;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import static java.awt.event.KeyEvent.getExtendedKeyCodeForChar;
import static java.lang.Integer.parseInt;
import static java.util.Collections.unmodifiableMap;
import static javax.swing.Action.ACCELERATOR_KEY;
import static javax.swing.Action.ACTION_COMMAND_KEY;
import static javax.swing.Action.DISPLAYED_MNEMONIC_INDEX_KEY;
import static javax.swing.Action.LARGE_ICON_KEY;
import static javax.swing.Action.LONG_DESCRIPTION;
import static javax.swing.Action.MNEMONIC_KEY;
import static javax.swing.Action.NAME;
import static javax.swing.Action.SHORT_DESCRIPTION;
import static javax.swing.Action.SMALL_ICON;
import static javax.swing.KeyStroke.getKeyStroke;
import static javax.swing.SwingUtilities.invokeAndWait;
import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.UIManager.getInstalledLookAndFeels;
import static javax.swing.UIManager.setLookAndFeel;

public enum SwingUtilities {
    ;

    private static final IconCache iconCache = new IconCache();
    private static final Map<String, Function<String, Object>> ACTION_CONVERTERS = createActionConverters();

    private static Map<String, Function<String, Object>> createActionConverters() {
        final Map<String, Function<String, Object>> actionConverters = new HashMap<>();
        // TODO Replace with lambda or method reference
        actionConverters.put(ACCELERATOR_KEY, new Function<String, Object>() {
            @Override
            public Object apply(final String s1) {
                return getKeyStroke(s1);
            }
        });
        // TODO Replace with lambda or method reference
        actionConverters.put(DISPLAYED_MNEMONIC_INDEX_KEY, new Function<String, Object>() {
            @Override
            public Object apply(final String s1) {
                return parseInt(s1);
            }
        });
        // TODO Replace with lambda or method reference
        actionConverters.put(NAME, new Function<String, Object>() {
            @Override
            public Object apply(final String s) {
                return s;
            }
        });
        // TODO Replace with lambda or method reference
        actionConverters.put(MNEMONIC_KEY, new Function<String, Object>() {
            @Override
            public Object apply(final String s) {
                return getExtendedKeyCodeForChar(s.codePointAt(0));
            }
        });
        // TODO Replace with lambda or method reference
        actionConverters.put(SHORT_DESCRIPTION, new Function<String, Object>() {
            @Override
            public Object apply(final String s) {
                return s;
            }
        });
        // TODO Replace with lambda or method reference
        actionConverters.put(LONG_DESCRIPTION, new Function<String, Object>() {
            @Override
            public Object apply(final String s) {
                return s;
            }
        });
        // TODO Replace with lambda or method reference
        actionConverters.put(SMALL_ICON, new Function<String, Object>() {
            @Override
            public Object apply(final String url) {
                return getImageIcon(url);
            }
        });
        // TODO Replace with lambda or method reference
        actionConverters.put(LARGE_ICON_KEY, new Function<String, Object>() {
            @Override
            public Object apply(final String url) {
                return getImageIcon(url);
            }
        });
        return unmodifiableMap(actionConverters);
    }

    public static <T extends Component> T findComponent(final Container container, final Class<T> componentClass) {
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

    public static <T> FutureTask<T> callLater(final Callable<T> callable) {
        final FutureTask<T> futureTask = new FutureTask<>(callable);
        invokeLater(futureTask);
        return futureTask;
    }

    public static void setLookAndFeelFromName(final String lookAndFeelName) {
        try {
            for (final LookAndFeelInfo info : getInstalledLookAndFeels()) {
                if (info.getName().equals(lookAndFeelName)) {
                    setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (final ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void initActionFromBundle(final Action action, final String actionCommand, final ResourceBundle resourceBundle) {
        action.putValue(ACTION_COMMAND_KEY, actionCommand);
        initActionFromBundle(action, resourceBundle);
    }

    public static void initActionFromBundle(final Action action, final ResourceBundle resourceBundle) {
        final String actionCommand = (String) action.getValue(ACTION_COMMAND_KEY);
        for (final Map.Entry<String, Function<String, Object>> entry : ACTION_CONVERTERS.entrySet()) {
            final String actionKey = entry.getKey();
            final Function<String, Object> function = entry.getValue();
            final String key = actionCommand + "." + actionKey;
            if (resourceBundle.containsKey(key)) {
                final String stringValue = resourceBundle.getString(key);
                final Object object = function.apply(stringValue);
                action.putValue(actionKey, object);
            }
        }
    }

    public static ImageIcon getImageIcon(final String urlString) {
        return iconCache.getImageIcon(urlString);
    }
}
