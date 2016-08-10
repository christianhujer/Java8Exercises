package com.nelkinda.training.java8.exercise5;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;

import static java.awt.BorderLayout.NORTH;
import static java.awt.event.KeyEvent.getExtendedKeyCodeForChar;
import static java.lang.Integer.parseInt;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Files.write;
import static java.util.ResourceBundle.getBundle;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.KeyStroke.getKeyStroke;
import static javax.swing.SwingUtilities.invokeAndWait;
import static javax.swing.SwingUtilities.updateComponentTreeUI;
import static javax.swing.UIManager.getInstalledLookAndFeels;
import static javax.swing.UIManager.setLookAndFeel;

public class Editor {
    private static final Map<String, Function<String, Object>> ACTION_CONVERTERS = new HashMap<>();
    private static final String UNNAMED = "<Unnamed>";

    static {
        // TODO Replace with lambda or method reference
        ACTION_CONVERTERS.put(Action.ACCELERATOR_KEY, new Function<String, Object>() {
            @Override public Object apply(final String s1) {
                return getKeyStroke(s1);
            }
        });
        // TODO Replace with lambda or method reference
        ACTION_CONVERTERS.put(Action.DISPLAYED_MNEMONIC_INDEX_KEY, new Function<String, Object>() {
            @Override public Object apply(final String s1) {
                return parseInt(s1);
            }
        });
        // TODO Replace with lambda or method reference
        ACTION_CONVERTERS.put(Action.NAME, new Function<String, Object>() {
            @Override public Object apply(final String s) {
                return s;
            }
        });
        // TODO Replace with lambda or method reference
        ACTION_CONVERTERS.put(Action.MNEMONIC_KEY, new Function<String, Object>() {
            @Override public Object apply(final String s) {
                return getExtendedKeyCodeForChar(s.codePointAt(0));
            }
        });
        // TODO Replace with lambda or method reference
        ACTION_CONVERTERS.put(Action.SHORT_DESCRIPTION, new Function<String, Object>() {
            @Override public Object apply(final String s) {
                return s;
            }
        });
        // TODO Replace with lambda or method reference
        ACTION_CONVERTERS.put(Action.SMALL_ICON, new Function<String, Object>() {
            @Override public Object apply(final String url) {
                return getImageIcon(url);
            }
        });
        // TODO Replace with lambda or method reference
        ACTION_CONVERTERS.put(Action.LARGE_ICON_KEY, new Function<String, Object>() {
            @Override public Object apply(final String url) {
                return getImageIcon(url);
            }
        });
    }

    private final JFrame frame;
    private final JEditorPane editorPane;
    private final ActionMap actions = new ActionMap();
    private final ResourceBundle resourceBundle = getBundle(getClass().getName());
    JFileChooser fileChooser = new JFileChooser();
    private String documentName = UNNAMED;
    private File file;
    private SwingWorker lastWorker;

    Editor() {
        frame = new JFrame("Editor: " + documentName);
        createActions();
        editorPane = new JEditorPane();
        frame.setJMenuBar(createJMenuBar());
        frame.getContentPane().add(createJToolBar(), NORTH);
        frame.getContentPane().add(new JScrollPane(editorPane));
        frame.pack();
        frame.setVisible(true);
    }

    private static ImageIcon getImageIcon(final String url) {
        final URL resource = Editor.class.getClassLoader().getResource(url);
        return resource != null ? new ImageIcon(resource) : null;
    }

    public static void main(final String... args) throws InvocationTargetException, InterruptedException {
        invokeAndWait(
                new Runnable() {
                    @Override public void run() {
                        setLookAndFeelFromName("Nimbus");
                        new Editor();
                    }
                }
        );
    }

    private static void setLookAndFeelFromName(final String lookAndFeelName) {
        try {
            for (final UIManager.LookAndFeelInfo info : getInstalledLookAndFeels()) {
                if (info.getName().equals(lookAndFeelName)) {
                    setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (final ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void initActionFromBundle(final Action action, final String actionCommand,
            final ResourceBundle resourceBundle) {
        action.putValue(Action.ACTION_COMMAND_KEY, actionCommand);
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

    private JToolBar createJToolBar() {
        final JToolBar toolBar = new JToolBar();
        for (final String actionCommand : "new - open - save saveAs".split("\\s+"))
            if ("-".equals(actionCommand) || "|".equals(actionCommand)) toolBar.addSeparator();
            else toolBar.add(actions.get(actionCommand));
        return toolBar;
    }

    private void createActions() {
        // TODO Replace with lambda or method reference
        createAction("new", new ActionListener() {
            @Override public void actionPerformed(final ActionEvent e) {
                newDocument(e);
            }
        });
        // TODO Replace with lambda or method reference
        createAction("open", new ActionListener() {
            @Override public void actionPerformed(final ActionEvent e) {
                open(e);
            }
        });
        // TODO Replace with lambda or method reference
        createAction("save", new ActionListener() {
            @Override public void actionPerformed(final ActionEvent e) {
                save(e);
            }
        });
        // TODO Replace with lambda or method reference
        createAction("saveAs", new ActionListener() {
            @Override public void actionPerformed(final ActionEvent e) {
                saveAs(e);
            }
        });
        // TODO Replace with lambda or method reference
        createAction("quit", new ActionListener() {
            @Override public void actionPerformed(final ActionEvent e) {
                quit(e);
            }
        });
    }

    private Action createAction(final String actionCommand, final ActionListener actionListener) {
        final Action action = new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                actionListener.actionPerformed(e);
            }
        };
        initActionFromBundle(action, actionCommand, resourceBundle);
        actions.put(actionCommand, action);
        return action;
    }

    private JMenuBar createJMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        // TODO Replace with lambda or method reference
        final JMenu file = new JMenu(createAction("file", new ActionListener() {
            @Override public void actionPerformed(final ActionEvent e) {
                dummy(e);
            }
        }));
        menuBar.add(file);
        for (final String actionCommand : "new open save saveAs - quit".split("\\s+"))
            if ("-".equals(actionCommand) || "|".equals(actionCommand)) file.addSeparator();
            else file.add(actions.get(actionCommand));

        // TODO Replace with lambda or method reference
        final JMenu lookAndFeel = new JMenu(createAction("lookAndFeel", new ActionListener() {
            @Override public void actionPerformed(final ActionEvent e) {
                dummy(e);
            }
        }));
        menuBar.add(lookAndFeel);
        for (final UIManager.LookAndFeelInfo lookAndFeelInfo : getInstalledLookAndFeels()) {
            final Action action = createAction("lookAndFeel:" + lookAndFeelInfo.getName(),
                    // TODO Replace with lambda or method reference
                    new ActionListener() {
                        @Override public void actionPerformed(final ActionEvent e) {
                            setLookAndFeelFromClassName(lookAndFeelInfo.getClassName());
                        }
                    });
            action.putValue(Action.NAME, lookAndFeelInfo.getName());
            lookAndFeel.add(action);
        }
        return menuBar;
    }

    private void setLookAndFeelFromClassName(final String className) {
        try {
            setLookAndFeel(className);
            updateComponentTreeUI(frame);
        } catch (final IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException | ClassNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    private void dummy(final ActionEvent e) {
    }

    private void newDocument(final ActionEvent e) {
        editorPane.setText("");
    }

    private void open(final ActionEvent e) {
        final int returnValue = fileChooser.showOpenDialog(frame);
        switch (returnValue) {
        case APPROVE_OPTION:
            setLastWorker(new Loader(fileChooser.getSelectedFile()));
            lastWorker.execute();
        }
    }

    private void save(final ActionEvent e) {
        if (file == null) saveAs(e);
        else {
            setLastWorker(new Saver(file));
            lastWorker.execute();
        }
    }

    private void saveAs(final ActionEvent e) {
        final int returnValue = fileChooser.showSaveDialog(frame);
        switch (returnValue) {
        case APPROVE_OPTION:
            setLastWorker(new Saver(fileChooser.getSelectedFile()));
            lastWorker.execute();
        }
    }

    void quit(final ActionEvent e) {
        frame.dispose();
    }

    String getDocumentName() {
        return documentName;
    }

    private void setFile(final File file) {
        this.file = file;
        this.documentName = file.getName();
        frame.setTitle("Editor: " + documentName);
    }

    JFrame getWindow() {
        return frame;
    }

    ActionMap getActions() {
        return actions;
    }

    synchronized SwingWorker getLastWorker() throws InterruptedException {
        SwingWorker lastWorker;
        while ((lastWorker = this.lastWorker) == null)
            wait();
        this.lastWorker = null;
        return lastWorker;
    }

    private synchronized void setLastWorker(final SwingWorker lastWorker) {
        this.lastWorker = lastWorker;
        notify();
    }

    private class Saver extends SwingWorker<Void, Void> {
        private final File file;

        Saver(final File file) {
            this.file = file;
        }

        @Override
        protected Void doInBackground() throws Exception {
            write(file.toPath(), editorPane.getText().getBytes("UTF-8"));
            // TODO Replace with lambda or method reference
            invokeAndWait(new Runnable() {
                @Override public void run() {
                    setFile(file);
                }
            });
            return null;
        }
    }

    private class Loader extends SwingWorker<Void, Void> {
        private final File file;

        Loader(final File file) {
            this.file = file;
        }

        @Override protected Void doInBackground() throws Exception {
            final String text = new String(readAllBytes(file.toPath()), "UTF-8");
            // TODO Replace with lambda or method reference
            invokeAndWait(new Runnable() {
                @Override public void run() {
                    editorPane.setText(text);
                    setFile(file);
                }
            });
            return null;
        }
    }
}
