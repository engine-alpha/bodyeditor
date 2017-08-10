package org.ea.bodyeditor.gui.editable;

import org.ea.bodyeditor.gui.BodyEditorFrame;
import org.ea.bodyeditor.gui.action.ChangePolyShapeAction;
import org.ea.bodyeditor.gui.tools.InfoPanel;
import org.ea.bodyeditor.gui.tools.InfoPanelContent;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        if(this.x == x && this.y == y) return; //<- Check if there's actual change. Otherwise ignore the command.
        setCoordinatesInternally(x,y);
        parent.pointUpdated();
    }


    public void setCoordinatesInternally(double x, double y) {
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
            if(highlighted){
                graphics2D.setStroke(new BasicStroke(1));
                graphics2D.setColor(pointStrokeColor);
                graphics2D.drawOval((int)(x-RADIUS), (int)(y-RADIUS), RADIUS*2, RADIUS*2);
            }
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

        setCoordinates(x+dX, y+dY);

        dX = dY = 0;

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
        if(parent instanceof PolygonFixtureProxy) {
            PolygonFixtureProxy pparent = ((PolygonFixtureProxy) parent);
            if(pparent.subPoints().size() <= 3) {
                pparent.processRemove();
                return;
            }
            PolygonShape old = pparent.createShapeFromCurrentPoints();
            PolygonShape newShape = pparent.getShapeWithout(this);
            BodyEditorFrame.doAction(new ChangePolyShapeAction(old,newShape, pparent));
        } else{
            //TODO CIRCLE Implementation -> Remove Circle
        }
    }



    private SpinnerNumberModel createPointNumberModel() {

        Double value = new Double(0);
        Double min = new Double(-10000);
        Double max = new Double(10000);
        Double step = new Double(0.01);

        return new SpinnerNumberModel(value, min, max, step);
    }

    public Vec2 coordinatesAsVec2() {
        Vec2 ret = new Vec2();
        ret.set((float)x, (float)y);
        return ret;
    }

    public FixtureProxy getParent() {
        return parent;
    }


    /* UI */

    @Override
    public InfoPanelContent createInfoPanelContent() {
        InfoPanelContent ret = new InfoPanelContent();

        ret.setLayout(new BoxLayout(ret, BoxLayout.PAGE_AXIS));

        ret.add(parent.createInfoPanelContent());

        String text;
        if(parent instanceof PolygonFixtureProxy) {
            text = "Polygon-Punkt";
        } else {
            //TODO: Kreis-Bezeichnungen
            text=null;
        }
        ret.add(InfoPanel.createTitle(text));

        JPanel edits = new JPanel();
        edits.setLayout(new GridLayout(2,2));

        JSpinner xSpinner = new JSpinner(createPointNumberModel());
        xSpinner.setValue(x);

        JSpinner ySpinner = new JSpinner(createPointNumberModel());
        ySpinner.setValue(y);

        ChangeListener cl = (e)-> {
                double doubleX = (double) xSpinner.getValue();
                double doubleY = (double) ySpinner.getValue();
                System.out.println("Set Coordinates: " + doubleX + " - " + doubleY);
                setCoordinates(doubleX, doubleY);
                System.out.println("--- Coordinates: " + x + " - " + y);
        };
        xSpinner.addChangeListener(cl);
        ySpinner.addChangeListener(cl);

        edits.add(InfoPanel.createText("x:"));
        edits.add(xSpinner);
        edits.add(InfoPanel.createText("y:"));
        edits.add(ySpinner);

        ret.add(edits);


        return ret;
    }


}
