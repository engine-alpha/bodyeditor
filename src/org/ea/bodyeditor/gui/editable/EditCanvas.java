package org.ea.bodyeditor.gui.editable;

import com.sun.xml.internal.ws.client.sei.ResponseBuilder;
import ea.BoundingRechteck;
import ea.raum.*;
import org.ea.bodyeditor.gui.BodyModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Das Edit-Canvas ist das Kern des Editors
 * Created by Michael on 08.08.2017.
 */
public class EditCanvas
extends JPanel
implements MouseListener, MouseMotionListener, MouseWheelListener {

    public static final int SCROLL_BIG = 50;
    public static final int SCROLL_NORMAL = 10;
    /**
     * Skalierungsfaktor für die Systemansicht
     */
    private double scale_factor;

    private double[] scale_factors = new double[] {
            0.01, 0.02, 0.04, 0.07, 0.1, 0.2, 0.4, 0.5, 1, 1.5, 2, 3, 5
    };
    private int scaleLevel = 8;

    private final List<Editable> elements = new LinkedList<Editable>();
    private final BodyModel bodyModel;
    /**
     * Gewünschte Dimension bei Scale-Factor 1.
     */
    private final Dimension baseline_dimension;

    /**
     * Das Scroll-Pane, in dem das Canvas liegt.
     */
    private JScrollPane scrollPane;

    public EditCanvas(BodyModel bodyModel) {
        this.bodyModel = bodyModel;

        baseline_dimension=calculatePreferredSize();

        this.setScale_factor(1);

        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
    }

    public void setScrollPane(JScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    /* RENDERING */

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        //0. Render Background
        Dimension size = this.getSize();
        g2d.setColor(Color.lightGray);
        g2d.fillRect(0,0, size.width, size.height);

        //Set to Scale
        AffineTransform transform = g2d.getTransform();
        transform.scale(scale_factor, scale_factor);
        g2d.setTransform(transform);

        //1. Render Grid


        //2. Render Raum
        bodyModel.getRaum().render(g2d);

        //3. Render Editables
        for(Editable e : elements) {
            e.render(g2d);
        }
    }

    /**
     * Berechnet die PreferredSize aus dem zugrunde liegenden Raum-Objekt
     */
    private Dimension calculatePreferredSize(){
        Raum raum = bodyModel.getRaum();
        if(raum instanceof Bild) {
            Bild bild = (Bild)raum;
            setPreferredSize(bild.getSize());
            return bild.getSize();
        } else if(raum instanceof Animation) {
            Animation animation = (Animation)raum;
            Dimension animation_dimension = new Dimension(animation.getImageWidth(), animation.getImageHeight());
            setPreferredSize(animation_dimension);
            return animation_dimension;
        }
        return null;
    }

    private void setScale_factor(double scale_factor) {
        this.scale_factor = scale_factor;

        //Update correct preferred size
        setPreferredSize(new Dimension(
                (int)(baseline_dimension.width*scale_factor),
                (int)(baseline_dimension.height*scale_factor)));

        if(scrollPane!=null) {
            scrollPane.updateUI();
            scrollPane.revalidate();
            scrollPane.repaint();
            this.revalidate();
            this.repaint();
        }

    }

    private void zoom(boolean in) {
        int change = in ? 1 : -1;
        int newIndex = scaleLevel+change;
        if(newIndex < 0 || newIndex >= scale_factors.length) return;
        scaleLevel = newIndex;
        setScale_factor(scale_factors[scaleLevel]);
    }

    /* MOUSE INTERFACE */

    @Override
    public void mouseClicked(MouseEvent e) {
        //Ignore
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int rotation = e.getWheelRotation();
        boolean altmasked = (e.getModifiers() & InputEvent.ALT_MASK) != 0;
        boolean shiftmasked = (e.getModifiers() & InputEvent.SHIFT_MASK) != 0;
        if((e.getModifiers() & InputEvent.CTRL_MASK) != 0) {
            //Zoom in / Out
            //Negative = Zoom in / Positive = Zoom out

            if(rotation == 0) return;
            zoom(rotation < 0);
        }

        //Scroll
        int scrollValue=(altmasked ? SCROLL_BIG : SCROLL_NORMAL);
        JScrollBar scrollBar = shiftmasked ? scrollPane.getHorizontalScrollBar() : scrollPane.getVerticalScrollBar();
        scrollBar.setValue(scrollBar.getValue()+rotation*scrollValue);
    }
}
