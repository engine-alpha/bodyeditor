package org.ea.bodyeditor.gui.action;

import org.ea.bodyeditor.gui.editable.EditCanvas;
import org.ea.bodyeditor.gui.editable.FixtureProxy;
import org.ea.bodyeditor.gui.editable.PointProxy;

/**
 * Created by Michael on 10.08.2017.
 */
public class AddFixtureAction
implements Action {

    private final FixtureProxy toAdd;
    private final EditCanvas from;

    public AddFixtureAction(FixtureProxy toAdd, EditCanvas from) {
        this.toAdd = toAdd;
        this.from = from;
    }

    @Override
    public void doAction() {
        from.addEditableElement(toAdd);
    }

    @Override
    public void undoAction() {
        from.removeEditableElement(toAdd);
        for(PointProxy pointProxy : toAdd.subPoints()) {
            from.removeEditableElement(pointProxy);
        }
    }
}
