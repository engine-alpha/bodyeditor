package org.ea.bodyeditor.gui.editable;

import org.ea.bodyeditor.gui.BodyEditorFrame;
import org.ea.bodyeditor.gui.action.ChangePolyShapeAction;
import org.ea.bodyeditor.gui.tools.InfoPanelContent;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

import java.awt.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by Michael on 09.08.2017.
 */
public class PolygonFixtureProxy
extends FixtureProxy {


    private PolygonShape polygonShape;

    /**
     * Liste aller
     */
    private List<PointProxy> pointProxyList=new LinkedList<>();


    public PolygonFixtureProxy(EditCanvas chief, FixtureDef fixtureDef) {
        super(chief, fixtureDef);
        setShape((PolygonShape)fixtureDef.shape);
    }

    @Override
    public void pointUpdated() {
        BodyEditorFrame.doAction(new ChangePolyShapeAction(
                (PolygonShape)fixtureDef.shape,
                this.createShapeFromCurrentPoints(),
                this
        ));
    }

    @Override
    public Collection<PointProxy> subPoints() {
        return pointProxyList;
    }

    public void setShape(PolygonShape shape) {
        removeAllPointProxies();
        polygonShape = shape;
        fixtureDef.shape = shape;
        Vec2[] vecs = polygonShape.getVertices();
        for(int i = 0; i < polygonShape.getVertexCount(); i++) {
            PointProxy pointProxy = new PointProxy(chief, this);
            pointProxy.setCoordinates(vecs[i].x, vecs[i].y);
            chief.addEditableElement(pointProxy);
            pointProxyList.add(pointProxy);
        }
        chief.updateUI();
        chief.repaint();
        chief.rewriteBodyModel();
    }

    private void removeAllPointProxies() {
        for(PointProxy proxy : pointProxyList) {
            chief.removeEditableElement(proxy);
        }
        pointProxyList.clear();
    }

    public PolygonShape createShapeFromCurrentPoints() {
        Vec2 vertices[] = new Vec2[pointProxyList.size()];
        for(int i = 0; i < vertices.length; i++) {
            vertices[i] = pointProxyList.get(i).coordinatesAsVec2();
        }
        PolygonShape polyShape = new PolygonShape();
        polyShape.set(vertices, vertices.length);
        return polyShape;
    }

    @Override
    public InfoPanelContent generateInfoPanelContent() {
        return null;
    }


    @Override
    public void render(Graphics2D graphics2D) {
        Vec2[] vec2s = polygonShape.getVertices();
        int[] xs = new int[polygonShape.getVertexCount()],
                ys = new int[polygonShape.getVertexCount()];
        for (int i = 0; i < xs.length; i++) {
            xs[i] = (int) (vec2s[i].x+dX );
            ys[i] = (int) (vec2s[i].y+dY );
        }
        //System.out.println("Render + " + dX +" - " + dY);
        graphics2D.setColor(dragged? shapeDragColor : shapeFillColor);
        graphics2D.fillPolygon(xs, ys, xs.length);

        graphics2D.setColor(shapeStrokeColor);
        graphics2D.setStroke(new BasicStroke(2));
        graphics2D.drawPolygon(xs, ys, xs.length);
    }

    @Override
    public void processDragMoveClick() {

    }

    @Override
    public void dragged() {
        for(PointProxy pointProxy : pointProxyList) {
            pointProxy.setRenderAble(false);
        }
    }


    @Override
    public void processMoveDragRelease() {
        for(PointProxy pointProxy : pointProxyList) {
            pointProxy.setRenderAble(true);
        }

        PolygonShape old = (PolygonShape)fixtureDef.shape;

        PolygonShape newShape = new PolygonShape();
        Vec2[] verticesOld = old.getVertices();
        Vec2[] verticesNew = new Vec2[old.getVertexCount()];
        for(int i = 0; i < verticesNew.length; i++){
            verticesNew[i] = new Vec2((float)(verticesOld[i].x+dX),(float) (verticesOld[i].y + dY));
        }
        newShape.set(verticesNew, verticesNew.length);

        BodyEditorFrame.doAction(new ChangePolyShapeAction(old, newShape, this));

        dX = dY = 0;
        dragged=false;
    }

}
