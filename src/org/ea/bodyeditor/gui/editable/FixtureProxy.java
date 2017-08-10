package org.ea.bodyeditor.gui.editable;

import ea.raum.Raum;
import org.ea.bodyeditor.gui.BodyEditorFrame;
import org.ea.bodyeditor.gui.action.RemoveFixtureAction;
import org.ea.bodyeditor.gui.tools.InfoPanel;
import org.ea.bodyeditor.gui.tools.InfoPanelContent;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

/**
 * Editable für eine Fixture.
 * Created by Michael on 09.08.2017.
 */
public abstract class FixtureProxy
extends Editable {

    protected final FixtureDef fixtureDef;


    /**
     * In der TestWord werden Fixtures für Collision Tests gebaut und sofort wieder gelöscht.
     */
    public static final World TEST_WORLD = new World(new Vec2());


    public FixtureProxy(EditCanvas chief, FixtureDef fixtureDef) {
        super(chief);
        this.fixtureDef = fixtureDef;
    }

    /**
     * Courtesy-Methode. Gibt eine funktional korrekte FixtureProxy für die angegebene Fixture aus.
     * @param fixtureDef    Die Fixture.
     * @return              Ein Proxy mit allen korrekten Eigenschaften.
     */
    public static FixtureProxy createFromFixture(EditCanvas chief, FixtureDef fixtureDef) {
        if(fixtureDef.shape==null) throw new RuntimeException("Fixture muss zum Einspeisen eine Shape haben.");
        if(fixtureDef.shape instanceof PolygonShape) {
            FixtureProxy ret = new PolygonFixtureProxy(chief, fixtureDef);
            return ret;
        }
        return null;
    }

    /* STATIC HELPERS */

    public static FixtureDef createStandardFixtureDef(Raum shapeOf) {
        FixtureDef ret = new FixtureDef();
        ret.shape = shapeOf.createShape(1);

        return ret;
    }

    @Override
    public boolean collisionCheck(ModelPoint logicalClick) {

        Body testbody = TEST_WORLD.createBody(new BodyDef());
        Fixture test = testbody.createFixture(fixtureDef);
        boolean ret = test.testPoint(new Vec2((float)logicalClick.x, (float)logicalClick.y));

        return ret;
    }

    public FixtureDef getFixture() {
        return fixtureDef;
    }

    public abstract void pointUpdated();

    @Override
    public void processRemove() {
        BodyEditorFrame.doAction(new RemoveFixtureAction(this, chief));
    }

    public abstract Collection<PointProxy> subPoints();


    /* UI */

    @Override
    public InfoPanelContent createInfoPanelContent() {
        InfoPanelContent ret = new InfoPanelContent();

        ret.setLayout(new BorderLayout());
        ret.add(InfoPanel.createTitle(getFixtureProxyTitle()), BorderLayout.NORTH);

        JPanel spinners = new JPanel();
        spinners.setLayout(new GridLayout(4,3));

        JSpinner densitySpinner = new JSpinner(createDensitiySpinnerNumberModel());
        densitySpinner.setValue(fixtureDef.density);

        spinners.add(InfoPanel.createText("Dichte:"));
        spinners.add(densitySpinner);
        spinners.add(InfoPanel.createText("kg/m²"));

        spinners.setMaximumSize(new Dimension(150, 1000));
        ret.add(spinners);

        return ret;
    }

    private static SpinnerNumberModel createDensitiySpinnerNumberModel() {
        SpinnerNumberModel ret = new SpinnerNumberModel();
        ret.setMinimum(0);
        ret.setMaximum(100000);
        return ret;
    }

    /**
     * Für die Angabe des korrekten Titels im UI
     * @return
     */
    protected abstract String getFixtureProxyTitle();
}
