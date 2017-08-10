package org.ea.bodyeditor.gui;

import ea.raum.Raum;
import org.ea.bodyeditor.gui.editable.FixtureProxy;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Das BodyModel sammelt alle Model-Informationen, die zu einem Body gehören. Das sind das Raum-Objekt und das
 * Body-Objekt (indirekt durch die Fixtures/Shapes).
 *
 * Created by Michael on 08.08.2017.
 */
public class BodyModel {

    private final Raum raum;

    private final List<FixtureDef> fixtureDefList;


    private BodyModel(Raum raum) {
        this.raum = raum;
        fixtureDefList = new LinkedList<>();
    }

    /**
     * Erstellt ein "leeres" BodyModel (mit Standard-Body).
     * @param raum  Das Raum-Objekt
     * @return      Das entstandende BodyModel
     */
    public static BodyModel createEmptyBodyModel(Raum raum) {
        BodyModel bodyModel = new BodyModel(raum);
        bodyModel.getFixtures().add(FixtureProxy.createStandardFixtureDef(raum));
        return bodyModel;
    }


    public Raum getRaum() {
        return raum;
    }

    public List<FixtureDef> getFixtures() {
        return fixtureDefList;
    }


    /* STATIC IN/OUTs */

    /**
     * Ließt ein Body-Model zum Bearbeiten ein.
     * @param inputStream
     * @return
     */
    public static BodyModel readBodyModel(InputStream inputStream) {
        //TODO
        return null;
    }

    /**
     * Schreibt ein BodyModel aus.
     * @param bodyModel
     * @param os
     */
    private static void writeBodyModel(BodyModel bodyModel, OutputStream os)
    throws IOException {

    }
}
