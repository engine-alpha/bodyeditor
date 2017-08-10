package org.ea.bodyeditor.gui.action;

/**
 * Beschreibt eine Handlung. Kann durchgeführt und Rückgängig gemacht werden.
 * Created by Michael on 09.08.2017.
 */
public interface Action {
    void doAction();
    void undoAction();
}
