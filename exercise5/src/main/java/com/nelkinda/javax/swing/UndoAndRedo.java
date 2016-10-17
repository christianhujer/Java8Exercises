package com.nelkinda.javax.swing;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionEvent;

public class UndoAndRedo implements UndoableEditListener {
    private final UndoManager undoManager = new UndoManager();
    private final AbstractUndoRedoAction undoAction = new UndoAction();
    private final AbstractUndoRedoAction redoAction = new RedoAction();

    @Override public void undoableEditHappened(final UndoableEditEvent e) {
        undoManager.undoableEditHappened(e);
        updateUndoAndRedoStates();
    }

    private void updateUndoAndRedoStates() {
        undoAction.updateState();
        redoAction.updateState();
    }

    public Action getUndoAction() {
        return undoAction;
    }

    public Action getRedoAction() {
        return redoAction;
    }

    private abstract class AbstractUndoRedoAction extends AbstractAction {
        AbstractUndoRedoAction() {
            setEnabled(false);
        }

        abstract void updateState();
    }

    private class RedoAction extends AbstractUndoRedoAction {
        @Override
        public void actionPerformed(final ActionEvent e) {
            undoManager.redo();
            updateUndoAndRedoStates();
        }

        @Override
        void updateState() {
            final boolean canRedo = undoManager.canRedo();
            setEnabled(canRedo);
            putValue(NAME, canRedo ? undoManager.getRedoPresentationName() : "Redo");
        }
    }

    private class UndoAction extends AbstractUndoRedoAction {
        @Override
        public void actionPerformed(final ActionEvent e) {
            undoManager.undo();
            updateUndoAndRedoStates();
        }

        @Override
        void updateState() {
            final boolean canUndo = undoManager.canUndo();
            setEnabled(canUndo);
            putValue(NAME, canUndo ? undoManager.getUndoPresentationName() : "Undo");
        }
    }
}
