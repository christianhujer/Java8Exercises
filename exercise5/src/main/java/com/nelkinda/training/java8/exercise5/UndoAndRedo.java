package com.nelkinda.training.java8.exercise5;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionEvent;

class UndoAndRedo implements UndoableEditListener {
    private final UndoManager undoManager = new UndoManager();
    private final UndoAction undoAction;
    private final RedoAction redoAction;

    UndoAndRedo() {
        undoAction = new UndoAction();
        redoAction = new RedoAction();
    }

    UndoManager getUndoManager() {
        return undoManager;
    }


    @Override public void undoableEditHappened(final UndoableEditEvent e) {
        updateUndoAndRedoStates();
    }

    private void updateUndoAndRedoStates() {
        undoAction.updateUndoState();
        redoAction.updateRedoState();
    }

    Action getUndoAction() {
        return undoAction;
    }

    Action getRedoAction() {
        return redoAction;
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
            putValue(NAME, canRedo ? undoManager.getRedoPresentationName() : "Redo");
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
            putValue(NAME, canUndo ? undoManager.getUndoPresentationName() : "Undo");
        }
    }
}
