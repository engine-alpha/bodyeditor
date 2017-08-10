package org.ea.bodyeditor.gui.editable;

import org.ea.bodyeditor.gui.BodyEditorFrame;
import org.ea.bodyeditor.gui.tools.InfoPanelContent;
import org.jbox2d.common.Vec2;

import java.awt.*;

/**
 * Created by Michael on 09.08.2017.
 */
public class PointProxy
extends Editable {


    private static final int RADIUS = 5;

    //Die Position
    private double x, y;

    //Ob der Punkt gerade gerendert werden soll
    private boolean renderMe=true;

    /**
     * Jeder Punkt gehört zu einem Fixture Proxy
     */
    private final FixtureProxy parent;



    public PointProxy(EditCanvas chief, FixtureProxy parent) {
        super(chief);
        this.parent = parent;
    }

    public void setCoordinates(double x, double y) {
        if(!BodyEditorFrame.isSnapEnabled()) {
            this.x = x;
            this.y = y;
        } else {
            //TODO: Implement better SNap
            this.x = (int)x;
            this.y = (int)y;
        }
    }

    public void setRenderAble(boolean render) {
        this.renderMe = render;
    }

    @Override
    public InfoPanelContent generateInfoPanelContent() {
        return null;
    }

    @Override
    public void render(Graphics2D graphics2D) {
        if(!renderMe) return;
        if(!dragged) {
            //System.out.println("Y");
            graphics2D.setColor(pointFillColor);
            graphics2D.fillOval((int)(x-RADIUS), (int)(y-RADIUS), RADIUS*2, RADIUS*2);
            graphics2D.setStroke(new BasicStroke(2));
            graphics2D.setColor(pointStrokeColor);
            graphics2D.drawOval((int)(x-RADIUS), (int)(y-RADIUS), RADIUS*2, RADIUS*2);
        } else {
            //System.out.println("RENDER");
            graphics2D.setColor(dragFillColor);
            graphics2D.fillOval((int)(x-RADIUS+dX), (int)(y-RADIUS+dY), RADIUS*2, RADIUS*2);
            graphics2D.setStroke(new BasicStroke(2));
            graphics2D.setColor(dragStrokeColor);
            graphics2D.drawOval((int)(x-RADIUS+dX), (int)(y-RADIUS+dY), RADIUS*2, RADIUS*2);
        }
    }

    @Override
    public void processDragMoveClick() {

    }


    @Override
    public void processMoveDragRelease() {
        dragged=false;

        x += dX;
        y += dY;

        dX = dY = 0;

        parent.pointUpdated();
    }

    @Override
    public boolean collisionCheck(ModelPoint logicalClick) {
        double a = logicalClick.x-x;
        double b = logicalClick.y-y;
        double distanceToPoint = Math.sqrt(a*a + b*b);
        return distanceToPoint<RADIUS;
    }

    @Override
    public void processRemove() {
        //TODO
    }

    public Vec2 coordinatesAsVec2() {
        Vec2 ret = new Vec2();
        ret.set((float)x, (float)y);
        return ret;
    }

    public FixtureProxy getParent() {
        return parent;
    }
}
