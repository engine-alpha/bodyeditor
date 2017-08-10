package org.ea.bodyeditor.gui.action;

import org.ea.bodyeditor.gui.editable.PolygonFixtureProxy;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;

/**
 * Created by Michael on 09.08.2017.
 */
public class ChangePolyShapeAction
implements Action {

    private final PolygonShape oldShape, newShape;
    private final PolygonFixtureProxy proxy;

    public ChangePolyShapeAction(PolygonShape oldShape, PolygonShape newShape, PolygonFixtureProxy proxy) {
        this.oldShape = oldShape;
        this.newShape = newShape;
        this.proxy = proxy;
    }

    @Override
    public void doAction() {
        proxy.setShape(newShape);
    }

    @Override
    public void undoAction() {
        proxy.setShape(oldShape);
    }
}
