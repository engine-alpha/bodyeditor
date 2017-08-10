package org.ea.bodyeditor.gui.action;

import org.ea.bodyeditor.gui.editable.EditCanvas;
import org.ea.bodyeditor.gui.editable.FixtureProxy;
import org.ea.bodyeditor.gui.editable.PointProxy;

/**
 * Created by Michael on 10.08.2017.
 */
public class RemoveFixtureAction
implements Action {


    private final FixtureProxy toRemove;
    private final EditCanvas from;

    public RemoveFixtureAction(FixtureProxy toRemove, EditCanvas from) {
        this.toRemove = toRemove;
        this.from = from;
    }

    @Override
    public void doAction() {
        from.removeEditableElement(toRemove);
        for(PointProxy pointProxy : toRemove.subPoints()) {
            from.removeEditableElement(pointProxy);
        }
    }

    @Override
    public void undoAction() {
        from.addEditableElement(toRemove);
        for(PointProxy pointProxy : toRemove.subPoints()) {
            from.addEditableElement(pointProxy);
        }
    }
}
