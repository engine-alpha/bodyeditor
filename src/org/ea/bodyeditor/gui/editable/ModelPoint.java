package org.ea.bodyeditor.gui.editable;

/**
 * Created by Michael on 09.08.2017.
 */
public class ModelPoint {

    public double x,y;

    public ModelPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public ModelPoint() {
        this(0.0,0.0);
    }
}
