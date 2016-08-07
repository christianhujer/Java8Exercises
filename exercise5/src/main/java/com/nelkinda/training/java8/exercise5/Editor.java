package com.nelkinda.training.java8.exercise5;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;

import static java.awt.event.KeyEvent.getExtendedKeyCodeForChar;

public class Editor {
    private static final Map<String, Function<String, Object>> initializers = new HashMap<>();

    static {
        initializers.put(Action.ACCELERATOR_KEY, KeyStroke::getKeyStroke);
        initializers.put(Action.DISPLAYED_MNEMONIC_INDEX_KEY, Integer::parseInt);
        initializers.put(Action.NAME, s -> s);
        initializers.put(Action.MNEMONIC_KEY, s -> getExtendedKeyCodeForChar(s.codePointAt(0)));
        initializers.put(Action.SHORT_DESCRIPTION, s -> s);
        initializers.put(Action.SMALL_ICON, Editor::getImageIcon);
        initializers.put(Action.LARGE_ICON_KEY, Editor::getImageIcon);
    }

    private final JFrame frame;
    private final JEditorPane editorPane;
    private final ActionMap actions = new ActionMap();
    private final ResourceBundle resourceBundle = ResourceBundle
            .getBundle("com.nelkinda.training.java8.exercise5.Editor");

    public Editor() {
        frame = new JFrame("Editor: Unnamed");
        createActions();
        editorPane = new JEditorPane();
        frame.setJMenuBar(createJMenuBar());
        frame.getContentPane().add(createJToolBar(), BorderLayout.NORTH);
        frame.getContentPane().add(new JScrollPane(editorPane));
        frame.pack();
    }

    private static ImageIcon getImageIcon(final String url) {
        URL resource = Editor.class.getClassLoader().getResource(url);
        return resource != null ? new ImageIcon(resource) : null;
    }

    public static void main(final String... args) {
        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            for (final UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (info.getName().equals("Nimbus")) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (final ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }
        new Editor().frame.setVisible(true);
    }

    private static void initActionFromBundle(final Action action, final String actionCommand,
            final ResourceBundle resourceBundle) {
        action.putValue(Action.ACTION_COMMAND_KEY, actionCommand);
        for (final Map.Entry<String, Function<String, Object>> entry : initializers.entrySet()) {
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
        createAction("new", this::newDocument);
        createAction("open", this::open);
        createAction("save", this::save);
        createAction("saveAs", this::saveAs);
        createAction("quit", this::quit);
    }

    public Action createAction(final String actionCommand, final ActionListener actionListener) {
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
        final JMenu file = new JMenu(createAction("file", this::dummy));
        menuBar.add(file);
        for (final String actionCommand : "new open save saveAs - quit".split("\\s+"))
            if ("-".equals(actionCommand) || "|".equals(actionCommand)) file.addSeparator();
            else file.add(actions.get(actionCommand));

        final JMenu lookAndFeel = new JMenu(createAction("lookAndFeel", this::dummy));
        menuBar.add(lookAndFeel);
        for (final UIManager.LookAndFeelInfo lookAndFeelInfo : UIManager.getInstalledLookAndFeels()) {
            final Action action = createAction("lookAndFeel:" + lookAndFeelInfo.getName(), e -> setLookAndFeel(lookAndFeelInfo.getClassName()));
            action.putValue(Action.NAME, lookAndFeelInfo.getName());
            lookAndFeel.add(action);
        }
        return menuBar;
    }

    private void setLookAndFeel(final String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (final IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException | ClassNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    public void dummy(final ActionEvent e) {
    }

    public void newDocument(final ActionEvent e) {
        editorPane.setText("");
    }

    public void open(final ActionEvent e) {
        throw new UnsupportedOperationException();
    }

    public void save(final ActionEvent e) {
        throw new UnsupportedOperationException();
    }

    public void saveAs(final ActionEvent e) {
        throw new UnsupportedOperationException();
    }

    public void quit(final ActionEvent e) {
        frame.dispose();
    }

    public String getDocumentName() {
        return "Unnamed";
    }

    public JFrame getWindow() {
        return frame;
    }

    public ActionMap getActions() {
        return actions;
    }
}
