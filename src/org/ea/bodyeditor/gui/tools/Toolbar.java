package org.ea.bodyeditor.gui.tools;

import ea.internal.io.ImageLoader;
import org.ea.bodyeditor.gui.BodyEditorFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.tools.Tool;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * The Toolbar provides a Toolset for different ways of manipulating the Body.
 * Created by Michael on 08.08.2017.
 */
public class Toolbar extends JPanel {

    private static final Toolbar TOOLBAR = new Toolbar();

    public static Toolbar getToolbar() {
        return TOOLBAR;
    }

    public static enum ToolMode {
        CIRCLE, POLYGON, MOVE, ADD, REMOVE;
    }

    public static ToolMode getActiveTool() {
        if(TOOLBAR.circle.isSelected()) return ToolMode.CIRCLE;
        if(TOOLBAR.polygon.isSelected()) return ToolMode.POLYGON;
        if(TOOLBAR.move.isSelected()) return ToolMode.MOVE;
        if(TOOLBAR.add.isSelected()) return ToolMode.ADD;
        if(TOOLBAR.remove.isSelected()) return ToolMode.REMOVE;

        return null;
    }

    private final JButton circle, polygon, move, add, remove;
    private final JButton[] all_buttons;

    private Toolbar() {
        this.setMinimumSize(new Dimension(100, 300));

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        JLabel title = new JLabel("Toolbar");
        title.setFont(new Font("Sans-Serif", Font.BOLD, 20));
        add(title);

        JPanel buttons = new JPanel();

        buttons.setLayout(new GridLayout(3, 2));

        circle = createButton(
                "Neue Kreis-Fixture", "circle.png", null);
        buttons.add(circle);

        polygon = createButton(
                "Neue konvexe Polygon-Fixture", "polygon.png", null);
        buttons.add(polygon);

        move = createButton(
                "Verschieben", "hand.png", null);
        buttons.add(move);

        add = createButton(
                "Punkt Hinzufügen (Polygon)", "plus.png", null);
        buttons.add(add);

        remove = createButton(
                "Objekt entfernen", "garbage.png", null);
        buttons.add(remove);

        buttons.setMaximumSize(new Dimension(120, 170));

        this.add(buttons);

        all_buttons = new JButton[] {
                circle, polygon, move, add, remove
        };


        move.setSelected(true);
    }

    private JButton createButton(String tooltip, String iconFileName,  ActionListener al) {
        JButton button = new JButton();
        button.setToolTipText(tooltip);
        button.setIcon(loadIcon(iconFileName));
        if(al != null)
            button.addActionListener(al);
        button.addActionListener((e)-> {
            for(JButton jb : all_buttons) {
                jb.setSelected(false);
            }
            button.setSelected(true);
        });
        return button;
    }

    private ImageIcon loadIcon(String iconFileName) {
        return new ImageIcon(ImageLoader.load("org/ea/bodyeditor/gui/tools/" + iconFileName));
    }

}
