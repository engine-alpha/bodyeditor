package org.ea.bodyeditor.gui.tools;

import org.ea.bodyeditor.gui.BodyEditorFrame;
import org.ea.bodyeditor.gui.editable.Editable;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Michael on 10.08.2017.
 */
public class InfoPanel
extends JPanel {

    public static final Font TITLE_FONT = new Font("Sans-Serif", Font.BOLD, 30);
    public static final Font TEXT_FONT = new Font("Sans-Serif", Font.PLAIN, 25);
    //public static final Font TITLE_FONT = new Font("Sans-Serif", Font.BOLD, 30);


    private static final InfoPanel INFO_PANEL = new InfoPanel();

    public static InfoPanel getInfoPanel() {
        return INFO_PANEL;
    }

    private InfoPanel() {
        setMinimumSize(new Dimension(500, 50));


    }

    /**
     * Löscht den bisherigen Inhalt des Info-Panels und ermöglicht einen neuen Inhalt.
     * @param content
     */
    public static void updateContent(InfoPanelContent content) {
        INFO_PANEL.removeAll();

        //INFO_PANEL.setLayout(new BoxLayout(INFO_PANEL, BoxLayout.PAGE_AXIS));

        INFO_PANEL.setLayout(new BorderLayout());
        INFO_PANEL.add(content, BorderLayout.CENTER);

        BodyEditorFrame.FRAME_INSTANCE.revalidate();
        //BodyEditorFrame.FRAME_INSTANCE.;

    }


    public static JLabel createTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(InfoPanel.TITLE_FONT);
        return label;
    }

    public static JLabel createText(String text) {
        JLabel label = new JLabel(text);
        label.setFont(InfoPanel.TEXT_FONT);
        return label;
    }

}
