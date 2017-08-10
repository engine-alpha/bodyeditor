package org.ea.bodyeditor.gui.action;


import org.ea.bodyeditor.gui.editable.PointProxy;
import org.jbox2d.collision.shapes.PolygonShape;

import java.awt.*;

/**
 * Created by Michael on 09.08.2017.
 */
public class MovePolyPointAction
        implements Action {

    private final PointProxy point;
    private final double oldX, oldY, newX, newY;

    //private final PolygonShape shape;

    public MovePolyPointAction(PointProxy point, double oldX, double oldY, double newX, double newY) {
        this.point = point;
        this.oldX = oldX;
        this.oldY = oldY;
        this.newX = newX;
        this.newY = newY;
    }

    @Override
    public void doAction() {
        point.setCoordinates(newX, newY);
        //point.getParent().getShape();
    }

    @Override
    public void undoAction() {
        point.setCoordinates(oldX, oldY);
    }
}
