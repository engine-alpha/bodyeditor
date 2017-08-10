package org.ea.bodyeditor.gui.editable;

import ea.Punkt;
import org.ea.bodyeditor.gui.tools.InfoPanel;
import org.ea.bodyeditor.gui.tools.InfoPanelContent;

import java.awt.*;

/**
 * Beschreibt ein mit der Maus editierbares Objekt im Editor.
 * Created by Michael on 08.08.2017.
 */
public abstract class Editable
implements Comparable<Editable> {


    public static final Color pointFillColor = new Color(255, 30, 100, 255);
    public static final Color pointStrokeColor = new Color(255, 157, 2, 255);

    public static final Color shapeFillColor = new Color(89, 255, 122, 100);
    public static final Color shapeStrokeColor = new Color(0, 157, 255, 200);
    public static final Color shapeDragColor = new Color(255, 219, 62, 100);

    public static final Color dragFillColor = new Color(116, 58, 255, 100);
    public static final Color dragStrokeColor = new Color(255, 116, 251, 100);


    public abstract InfoPanelContent generateInfoPanelContent();

    protected final EditCanvas chief;

    protected boolean highlighted=false;

    //Für Drag Effect
    protected double dX=0, dY=0;

    //Ob der Proxy gerade gedragged wird.
    protected boolean dragged=false;

    public Editable(EditCanvas chief) {
        this.chief = chief;
    }

    /**
     * Rendert das Editable gemäß seiner (ihm bekannten) Position aus.
     * @param graphics2D    Das Graphics-Objekt
     */
    public abstract void render(Graphics2D graphics2D);

    /**
     * Logik bei einem Click. Wird für alle Editables aufgerufen.
     */
    public abstract void processDragMoveClick();

    /**
     * Verarbeitet einen Drag (noch keinen Release)
     * @param dx    dX seit Press
     * @param dy    dY seit Press
     */
    public void processMoveDrag(double dx, double dy) {
        //System.out.println("Drag " + dx + " - " + dy);
        dragged=true;
        dX = dx;
        dY = dy;
        chief.repaint();
        dragged();
    }

    public void dragged() {
        //Leer - zum Überschreiben
    }

    /**
     * Verarbeitet einen Release
     */
    public abstract void processMoveDragRelease();

    /**
     * Führt einen Check aus, ob dieses Objekt gerade angeklickt würde.
     * @param logicalClick der Punkt des Klicks. Logisch umgerechnet auf px
     * @return  TRUE, wenn das Objekt angeklickt wird, sonst false.
     */
    public abstract boolean collisionCheck(ModelPoint logicalClick);

    /**
     * Verarbeitet den Remove dieses Objekts
     */
    public abstract void processRemove();

    /**
     * Erstellt den Info-Panel-Content, um dieses Objekt zu bearbeiten.
     * @return Info-Panel-Content, um dieses Objekt zu bearbeiten.
     */
    public abstract InfoPanelContent createInfoPanelContent();

    /**
     * Comparable Methode
     * @param other
     * @return
     */
    @Override
    public int compareTo(Editable other) {
        if(this instanceof PointProxy && other instanceof FixtureProxy) {
            return 1;
        } else if (this instanceof FixtureProxy && other instanceof PointProxy) {
            return -1;
        }
        return 0;
    }

    /**
     * Setzt, ob dieses Editable-Objekt gerade highlighted werden soll.
     * @param highlighted
     */
    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;

        if(highlighted) {
            //Update InfoPanel

        }
    }
}
