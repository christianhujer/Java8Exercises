package com.nelkinda.training.java8.exercise5;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;
import java.awt.*;
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

    final JFileChooser fileChooser = new JFileChooser();
    private final JEditorPane editorPane = new JEditorPane();
    private final UndoManager undoManager = new UndoManager();
    private final ActionMap actions = new ActionMap();
    private final ResourceBundle resourceBundle = getBundle(getClass().getName());
    private String documentName = UNNAMED;
    private final JFrame frame = new JFrame("Editor: " + documentName);
    private File file;
    private SwingWorker lastWorker;

    Editor() {
        createActions();
        final Document document = editorPane.getDocument();
        document.addUndoableEditListener(undoManager);
        // TODO Replace with lambda or method reference
        document.addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(final UndoableEditEvent e) {
                updateUndoAndRedoStates();
            }
        });
        frame.setJMenuBar(createJMenuBar());
        frame.getContentPane().add(new JScrollPane(editorPane));
        frame.getContentPane().add(createJToolBar(), NORTH);
        frame.pack();
        frame.setVisible(true);
    }

    private static ImageIcon getImageIcon(final String url) {
        final URL resource = Editor.class.getClassLoader().getResource(url);
        return resource != null ? new ImageIcon(resource) : null;
    }

    public static void main(final String... args) throws InvocationTargetException, InterruptedException {
        // TODO Replace with lambda or method reference
        invokeAndWait(new Runnable() {
            @Override public void run() {
                setLookAndFeelFromName("Nimbus");
                new Editor();
            }
        });
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

    private void updateUndoAndRedoStates() {
        ((UndoAction) actions.get("undo")).updateUndoState();
        ((RedoAction) actions.get("redo")).updateRedoState();
    }

    private JToolBar createJToolBar() {
        final JToolBar toolBar = new JToolBar();
        toolBar.setFocusable(false);
        for (final String actionCommand : "new open save saveAs - undo redo - cut-to-clipboard copy-to-clipboard paste-from-clipboard"
                .split("\\s+"))
            if ("-".equals(actionCommand) || "|".equals(actionCommand)) toolBar.addSeparator();
            else toolBar.add(actions.get(actionCommand));
        for (final Component c : toolBar.getComponents())
            c.setFocusable(false);
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
        createAction("cut-to-clipboard", new DefaultEditorKit.CutAction());
        createAction("copy-to-clipboard", new DefaultEditorKit.CopyAction());
        createAction("paste-from-clipboard", new DefaultEditorKit.PasteAction());
        createAction("undo", new UndoAction());
        createAction("redo", new RedoAction());
    }

    private Action createAction(final String actionCommand, final ActionListener actionListener) {
        final Action action = actionListener instanceof Action ? (Action) actionListener : new AbstractAction() {
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
        createMenu(file, "new open save saveAs - quit");

        // TODO Replace with lambda or method reference
        final JMenu edit = new JMenu(createAction("edit", new ActionListener() {
            @Override public void actionPerformed(final ActionEvent e) {
                dummy(e);
            }
        }));
        menuBar.add(edit);
        createMenu(edit, "undo redo | cut-to-clipboard copy-to-clipboard paste-from-clipboard");

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

    private void createMenu(final JMenu menu, final String menuDescription) {
        for (final String actionCommand : menuDescription.split("\\s+"))
            if ("-".equals(actionCommand) || "|".equals(actionCommand)) menu.addSeparator();
            else menu.add(actions.get(actionCommand));
    }

    private void setLookAndFeelFromClassName(final String className) {
        try {
            setLookAndFeel(className);
            updateComponentTreeUI(frame);
        } catch (final IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException | ClassNotFoundException e) {
            e.printStackTrace();
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
            runWorker(new Loader(fileChooser.getSelectedFile()));
        }
    }

    private void save(final ActionEvent e) {
        if (file == null) saveAs(e);
        else runWorker(new Saver(file));
    }

    private void saveAs(final ActionEvent e) {
        final int returnValue = fileChooser.showSaveDialog(frame);
        switch (returnValue) {
        case APPROVE_OPTION:
            runWorker(new Saver(fileChooser.getSelectedFile()));
        }
    }

    private void quit(final ActionEvent e) {
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

    private synchronized void runWorker(final SwingWorker lastWorker) {
        this.lastWorker = lastWorker;
        lastWorker.execute();
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

    private class UndoAction extends AbstractAction {
        UndoAction() {
            setEnabled(false);
        }

        @Override public void actionPerformed(final ActionEvent e) {
            undoManager.undo();
            updateUndoAndRedoStates();
        }

        void updateUndoState() {
            final boolean canUndo = undoManager.canUndo();
            setEnabled(canUndo);
            putValue(Action.NAME, canUndo ? undoManager.getUndoPresentationName() : "Undo");
        }
    }

    private class RedoAction extends AbstractAction {
        RedoAction() {
            setEnabled(false);
        }

        @Override public void actionPerformed(final ActionEvent e) {
            undoManager.redo();
            updateUndoAndRedoStates();
        }

        void updateRedoState() {
            final boolean canRedo = undoManager.canRedo();
            setEnabled(canRedo);
            putValue(Action.NAME, canRedo ? undoManager.getRedoPresentationName() : "Redo");
        }
    }
}
