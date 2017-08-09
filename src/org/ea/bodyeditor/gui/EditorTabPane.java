package org.ea.bodyeditor.gui;

import ea.raum.Raum;
import org.ea.bodyeditor.gui.editable.EditCanvas;

import javax.swing.*;
import java.awt.*;

/**
 * Das TabbedPane, das die Bearbeitung mehrerer Collider in einem Fenster ermöglicht.
 * Created by Michael on 08.08.2017.
 */
public class EditorTabPane
extends JTabbedPane {

    public EditorTabPane() {
        setMinimumSize(new Dimension(400,400));
    }

    public void newEditTab(String tabTitle, BodyModel bodyModel) {
        JScrollPane scrollPane = new JScrollPane(new EditCanvas(bodyModel));

        super.addTab(tabTitle, scrollPane);
    }

}
