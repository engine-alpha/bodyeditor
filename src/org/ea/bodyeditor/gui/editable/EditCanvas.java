package org.ea.bodyeditor.gui.editable;

import ea.raum.*;
import org.ea.bodyeditor.gui.BodyEditorFrame;
import org.ea.bodyeditor.gui.BodyModel;
import org.ea.bodyeditor.gui.action.AddFixtureAction;
import org.ea.bodyeditor.gui.tools.InfoPanel;
import org.ea.bodyeditor.gui.tools.Toolbar;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;

import javax.swing.*;
import java.awt.*;
import java.awt.Polygon;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Das Edit-Canvas ist das Kern des Editors
 * Created by Michael on 08.08.2017.
 */
public class EditCanvas
extends JPanel
implements MouseListener, MouseMotionListener, MouseWheelListener,ComponentListener {

    public static final int SCROLL_BIG = 50;
    public static final int SCROLL_NORMAL = 10;

    private Point visual_start_point= new Point(0,0);
    private ModelPoint lastClick = null;
    private Editable mouse_drag_selection = null;

    /**
     * Skalierungsfaktor für die Systemansicht
     */
    private double scale_factor;

    private double[] scale_factors = new double[] {
            0.01, 0.02, 0.04, 0.07, 0.1, 0.2, 0.4, 0.5, 1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5, 5
    };
    private int scaleLevel = 8;

    private final List<Editable> elements = new LinkedList<Editable>();
    private BodyModel bodyModel;
    /**
     * Gewünschte Dimension bei Scale-Factor 1.
     */
    private final Dimension baseline_dimension;

    /**
     * Das Scroll-Pane, in dem das Canvas liegt.
     */
    private JScrollPane scrollPane;

    //Für Drag
    private boolean mousePressed=false;

    public EditCanvas(BodyModel bodyModel) {
        this.bodyModel = bodyModel;

        baseline_dimension= processInputGraphics();
        processInputFixtures();

        this.setScale_factor(1);

        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
        this.addComponentListener(this);
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
        transform.translate(visual_start_point.x, visual_start_point.y);
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
     * Laedt die Fixtures des BodyModel im konstruktur ein
     */
    private void processInputFixtures() {
        List<FixtureDef> fixtureDefs = bodyModel.getFixtures();
        if(fixtureDefs.isEmpty()) throw new RuntimeException("BodyModel hatte keine Fixtures");
        for(FixtureDef fixtureDef : fixtureDefs) {
            FixtureProxy proxy = FixtureProxy.createFromFixture(this, fixtureDef);
            elements.add(proxy);
        }
    }

    /**
     * Schreibt das gesamte BodyModel neu gemäß der aktuellen States der Fixtures.
     */
    public void rewriteBodyModel() {
        Raum r = bodyModel.getRaum();

        BodyModel newBodyModel = BodyModel.createEmptyBodyModel(r);
        for(Editable e : elements) {
            if(e instanceof FixtureProxy)
                newBodyModel.getFixtures().add(((FixtureProxy)e).fixtureDef);
        }
        bodyModel = newBodyModel;

    }

    /**
     * Berechnet die PreferredSize aus dem zugrunde liegenden Raum-Objekt und pflegt das Input-Raum-Objekt in das
     * Canvas ein.
     * @return die Bounding-Dimension des Raum-Objekts
     */
    private Dimension processInputGraphics(){
        Raum raum = bodyModel.getRaum();
        if(raum instanceof Bild) {
            Bild bild = (Bild)raum;
            setPreferredSize(bild.getSize());
            return bild.getSize();
        } else if(raum instanceof Animation) {
            Animation animation = (Animation)raum;
            Dimension animation_dimension = new Dimension(animation.getImageWidth(), animation.getImageHeight());
            setPreferredSize(animation_dimension);
            new AnimationThread(animation, 100).start();
            return animation_dimension;
        }
        return null;
    }

    public void addEditableElement(Editable e) {
        elements.add(e);
        Collections.sort(elements);
        if(e instanceof FixtureProxy) {
            rewriteBodyModel();
        }
    }

    public void removeEditableElement(Editable editable) {
        elements.remove(editable);
        if(editable instanceof FixtureProxy) {
            rewriteBodyModel();
        }
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
        updateStartPoint();
    }

    private void updateStartPoint() {
        Dimension size = this.getSize();
        Dimension toCheckWith = new Dimension((int)(baseline_dimension.width*scale_factor),
                (int)(baseline_dimension.height*scale_factor));
        if(toCheckWith.width < size.width && toCheckWith.height < size.height) {
            //Component echt Größer als zu renderndes Objekt: Startpoint anpassen
            Point center = new Point(size.width/2, size.height/2);
            visual_start_point = new Point(center.x- (toCheckWith.width/2),center.y - (toCheckWith.height/2));
        } else {
            //Component ist kleiner als zu renderndes Objekt: start_piont=0,0
            visual_start_point=new Point(0,0);
        }
    }


    /* MOUSE INTERFACE */

    /**
     *
     * @param pointOnComponent Der Mauspunkt auf dem Component
     * @return  Der Mousepunkt im Model
     */
    private ModelPoint processMousePoint(Point pointOnComponent) {
        ModelPoint point = new ModelPoint();
        point.x = ((pointOnComponent.x - visual_start_point.x)/scale_factor);
        point.y = ((pointOnComponent.y - visual_start_point.y)/scale_factor);
        return point;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //Ignore
    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastClick = processMousePoint(e.getPoint()); //<- Click auf JPanel
        if(e.getButton() == MouseEvent.BUTTON1) {

            if(Toolbar.getActiveTool() == Toolbar.ToolMode.POLYGON) {
                //Create default Polygon
                FixtureDef def = new FixtureDef();
                PolygonShape polygonShape = new PolygonShape();
                polygonShape.setAsBox(50, 50, new Vec2((float)lastClick.x, (float)lastClick.y), 45);
                def.shape = polygonShape;
                PolygonFixtureProxy pfg = new PolygonFixtureProxy(this, def);
                BodyEditorFrame.doAction(new AddFixtureAction(pfg, this));

                return;
            }

            mousePressed = true;

            mouse_drag_selection = null;


            Editable clicked = findPotentialSelection(lastClick, false);
            if(clicked == null) return;

            switch (Toolbar.getActiveTool()) {
                case MOVE:
                    clicked.processDragMoveClick();
                    mouse_drag_selection = clicked;
                    InfoPanel.updateContent(clicked.createInfoPanelContent());
                    break;
                case REMOVE:
                    clicked.processRemove();
                    break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(Toolbar.getActiveTool() != Toolbar.ToolMode.MOVE) return;
        if(e.getButton() == MouseEvent.BUTTON1) {
            mousePressed = false;
            if(mouse_drag_selection != null) {
                mouse_drag_selection.processMoveDragRelease();
            }
            mouse_drag_selection = null;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(Toolbar.getActiveTool() != Toolbar.ToolMode.MOVE) return;
        ModelPoint draggedPoint = processMousePoint(e.getPoint());
        if(mouse_drag_selection != null) {
            mouse_drag_selection.processMoveDrag(draggedPoint.x - lastClick.x,
                    draggedPoint.y - lastClick.y);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        ModelPoint mousePosition = processMousePoint(e.getPoint()); //<- Click auf JPanel
        if(mouse_drag_selection == null) {
            //Is not dragging => Highlight Selection

            //Update Highlights, ignore Selection for now
            findPotentialSelection(mousePosition, true);

            this.repaint();
        }
    }

    /**
     * Iteriert alle Editables und gibt die potentielle Auswahl aus.
     * @param updateHighlights      TRUE= setHighlighted(...) wird korrekt durchgegeben
     * @param mousePositionOnCanvas Die Mausposition, von der ausgegangen wird.
     * @return                      null (= kein Editable gefunden) oder das auszuwählende Editable.
     */
    private Editable findPotentialSelection(ModelPoint mousePositionOnCanvas, boolean updateHighlights) {
        Editable result = null;
        for(Editable editable : elements) {
            if(result==null && editable.collisionCheck(mousePositionOnCanvas)) {
                //System.out.println("HIGHLIGHT " + editable);
                result = editable;
                if(updateHighlights) editable.setHighlighted(true);
            } else {
                if(updateHighlights) editable.setHighlighted(false);
            }
        }
        return result;
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

    @Override
    public void componentResized(ComponentEvent e) {
        updateStartPoint();
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }



    /* THREAD FOR ANIMATION STATES */

    private final class AnimationThread
    extends Thread {

        /**
         * Die upzudatende Animation
         */
        private final Animation animation;

        /**
         * Die Wartezeit zwischen Frameupdates
         */
        private final int wait_time;

        private AnimationThread(Animation animation, int wait_time) {
            setDaemon(true);
            this.animation = animation;
            this.wait_time = wait_time;
        }

        @Override
        public void run() {
            while(!interrupted()) {
                animation.onFrameUpdate(wait_time);
                EditCanvas.this.repaint();
                try {
                    Thread.sleep(wait_time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
