package org.ea.bodyeditor.gui.editable;

import ea.raum.*;
import org.ea.bodyeditor.gui.BodyModel;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Das Edit-Canvas ist das Kern des Editors
 * Created by Michael on 08.08.2017.
 */
public class EditCanvas
extends JPanel
implements MouseListener, MouseMotionListener {

    private final List<Editable> elements = new LinkedList<Editable>();
    private final BodyModel bodyModel;

    public EditCanvas(BodyModel bodyModel) {
        this.bodyModel = bodyModel;
        super.setMinimumSize(new Dimension(400, 400));
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        //1. Render Grid


        //2. Render Raum
        bodyModel.getRaum().render(g2d);

        //3. Render Editables
        for(Editable e : elements) {
            e.render(g2d);
        }
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
}
