package org.ea.bodyeditor.gui.editable;

import org.jbox2d.dynamics.FixtureDef;

/**
 * Editable für eine Fixture.
 * Created by Michael on 09.08.2017.
 */
public abstract class FixtureProxy
extends Editable {

    private final FixtureDef fixtureDef;

    public FixtureProxy(FixtureDef fixtureDef) {
        this.fixtureDef = fixtureDef;
    }
}
