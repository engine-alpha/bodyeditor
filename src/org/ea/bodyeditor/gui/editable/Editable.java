package org.ea.bodyeditor.gui.editable;

import org.ea.bodyeditor.gui.tools.InfoPanelContent;

import java.awt.*;

/**
 * Beschreibt ein editierbares Objekt im Editor.
 * Created by Michael on 08.08.2017.
 */
public abstract class Editable {

    public abstract InfoPanelContent generateInfoPanelContent();

    /**
     * Rendert das Editable gemäß seiner (ihm bekannten) Position aus.
     * @param graphics2D    Das Graphics-Objekt
     */
    public abstract void render(Graphics2D graphics2D);

}
