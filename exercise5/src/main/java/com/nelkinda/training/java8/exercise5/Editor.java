package com.nelkinda.training.java8.exercise5;

import com.nelkinda.javax.swing.UndoAndRedo;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;

import static com.nelkinda.javax.swing.SwingUtilities.initActionFromBundle;
import static com.nelkinda.javax.swing.SwingUtilities.setLookAndFeelFromName;
import static java.awt.BorderLayout.NORTH;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Files.write;
import static java.util.ResourceBundle.getBundle;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.SwingUtilities.invokeAndWait;
import static javax.swing.SwingUtilities.updateComponentTreeUI;
import static javax.swing.UIManager.getInstalledLookAndFeels;
import static javax.swing.UIManager.setLookAndFeel;

public class Editor {
    private static final String UNNAMED = "<Unnamed>";

    final JFileChooser fileChooser = new JFileChooser();
    private final JEditorPane editorPane = new JEditorPane();
    private final UndoAndRedo undoAndRedo = new UndoAndRedo();
    private final ActionMap actions = new ActionMap();
    private final ResourceBundle resourceBundle = getBundle(getClass().getName());
    private String documentName = UNNAMED;
    private final JFrame frame = new JFrame("Editor: " + documentName);
    private File file;
    private SwingWorker lastWorker;

    Editor() {
        createActions();
        final Document document = editorPane.getDocument();
        document.addUndoableEditListener(undoAndRedo);
        frame.setJMenuBar(createJMenuBar());
        frame.getContentPane().add(new JScrollPane(editorPane));
        frame.getContentPane().add(createJToolBar(), NORTH);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(final String... args) throws InvocationTargetException, InterruptedException {
        // TODO Replace with lambda or method reference
        invokeAndWait(new Runnable() {
            @Override
            public void run() {
                setLookAndFeelFromName("Nimbus");
                new Editor();
            }
        });
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
            @Override
            public void actionPerformed(final ActionEvent e) {
                newDocument(e);
            }
        });
        // TODO Replace with lambda or method reference
        createAction("open", new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                open(e);
            }
        });
        // TODO Replace with lambda or method reference
        createAction("save", new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                save(e);
            }
        });
        // TODO Replace with lambda or method reference
        createAction("saveAs", new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                saveAs(e);
            }
        });
        // TODO Replace with lambda or method reference
        createAction("quit", new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                quit(e);
            }
        });
        createAction("cut-to-clipboard", new DefaultEditorKit.CutAction());
        createAction("copy-to-clipboard", new DefaultEditorKit.CopyAction());
        createAction("paste-from-clipboard", new DefaultEditorKit.PasteAction());
        createAction("undo", undoAndRedo.getUndoAction());
        createAction("redo", undoAndRedo.getRedoAction());
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
            @Override
            public void actionPerformed(final ActionEvent e) {
                dummy(e);
            }
        }));
        menuBar.add(file);
        createMenu(file, "new open save saveAs - quit");

        // TODO Replace with lambda or method reference
        final JMenu edit = new JMenu(createAction("edit", new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                dummy(e);
            }
        }));
        menuBar.add(edit);
        createMenu(edit, "undo redo | cut-to-clipboard copy-to-clipboard paste-from-clipboard");

        // TODO Replace with lambda or method reference
        final JMenu lookAndFeel = new JMenu(createAction("lookAndFeel", new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                dummy(e);
            }
        }));
        menuBar.add(lookAndFeel);
        for (final UIManager.LookAndFeelInfo lookAndFeelInfo : getInstalledLookAndFeels()) {
            final Action action = createAction("lookAndFeel:" + lookAndFeelInfo.getName(),
                    // TODO Replace with lambda or method reference
                    new ActionListener() {
                        @Override
                        public void actionPerformed(final ActionEvent e) {
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
        setFile(null);
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
        if (file != null)
            documentName = file.getName();
        else
            documentName = "<Unnamed>";
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
                @Override
                public void run() {
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

        @Override
        protected Void doInBackground() throws Exception {
            final String text = new String(readAllBytes(file.toPath()), "UTF-8");
            // TODO Replace with lambda or method reference
            invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    editorPane.setText(text);
                    setFile(file);
                }
            });
            return null;
        }
    }
}
