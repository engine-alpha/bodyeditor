package org.ea.bodyeditor.gui;

import ea.raum.Raum;
import org.ea.bodyeditor.gui.tools.InfoPanel;
import org.ea.bodyeditor.gui.tools.Toolbar;


import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;
import javax.tools.Tool;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Stack;

import org.ea.bodyeditor.gui.action.Action;

/**
 * The Software's main frame. Contains all UI Elements.
 * Created by Michael on 08.08.2017.
 */
public class BodyEditorFrame
extends JFrame  {

    /**
     * Software's Frame Title
     */
    public static final String FRAME_TITLE = "Engine Alpha - Body Editor";

    /**
     * Die Toolbar des Editors
     */
    private final Toolbar toolbar;

    /**
     * Checkbox für Snap funktion
     */
    private static JCheckBoxMenuItem snap;

    private static final Stack<Action> undo_stack = new Stack<>();
    private static final Stack<Action> redo_stack = new Stack<>();

    public static boolean isSnapEnabled() {
        return snap.isSelected();
    }

    private final EditorTabPane tabPane;

    public static final BodyEditorFrame FRAME_INSTANCE=new BodyEditorFrame();

    private BodyEditorFrame() {
        super(FRAME_TITLE);

        //Create MenuBar
        JMenuBar jMenu = createEditorMenuBar();
        super.setJMenuBar(jMenu);


        //Create Content
        super.getContentPane().setLayout(new BorderLayout());
        super.getContentPane().add(toolbar= Toolbar.getToolbar(), BorderLayout.WEST);
        super.getContentPane().add(tabPane=new EditorTabPane(), BorderLayout.CENTER);
        super.getContentPane().add(InfoPanel.getInfoPanel(), BorderLayout.EAST);


        //Dimension Setting
        super.setMinimumSize(new Dimension(400, 400));
        super.setPreferredSize(new Dimension(1080, 800));

        pack();
    }


    /* DO & UNDO */

    public static void doAction(Action a) {
        a.doAction();
        undo_stack.add(a);
        redo_stack.removeAllElements();
    }

    public static void undoAction() {
        if(undo_stack.isEmpty()) return;
        Action a = undo_stack.pop();
        redo_stack.add(a);
        a.undoAction();
    }

    public static void redoAction() {
        if(redo_stack.isEmpty()) return;
        Action a = redo_stack.pop();
        if(a == null) return;
        undo_stack.add(a);
        a.doAction();
    }

    /* MENU BAR */

    private final JMenuBar createEditorMenuBar() {
        JMenuBar jMenuBar = new JMenuBar();

        //Datei
        JMenu datei = new JMenu("Datei");

        datei.add(createMenuItem("Neu aus Bild...", () -> newBodyModel()));
        datei.add(createMenuItem("Öffnen...", () -> System.out.println("Helloo")));
        datei.add(createMenuItem("Speichern", () -> System.out.println("Helloo")));
        datei.add(createMenuItem("Speichern unter...", () -> System.out.println("Helloo")));
        datei.add(createMenuItem("Einstellungen...", () -> System.out.println("Helloo")));

        jMenuBar.add(datei);

        //Bearbeiten
        JMenu bearbeiten = new JMenu("Bearbeiten");

        JMenuItem doA, undoA;
        bearbeiten.add(doA = createMenuItem("Rückgängig", () -> undoAction()));
        doA.setAccelerator(KeyStroke.getKeyStroke('Z', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
        bearbeiten.add(undoA = createMenuItem("Wiederherstellen", () -> redoAction()));
        undoA.setAccelerator(KeyStroke.getKeyStroke('Y', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));

        jMenuBar.add(bearbeiten);

        //Ansicht
        JMenu ansicht = new JMenu("Ansicht");

        jMenuBar.add(ansicht);

        //Tools
        JMenu tools = new JMenu("Tools");

        snap = new JCheckBoxMenuItem("Snap to Pixel");
        tools.add(snap);

        jMenuBar.add(tools);

        //Hilfe
        JMenu hilfe = new JMenu("Hilfe");

        hilfe.add(createMenuItem("Über diese Software", () -> System.out.println("Helloo")));

        jMenuBar.add(hilfe);

        return jMenuBar;
    }

    private JMenuItem createMenuItem(String text, final Runnable action) {
        JMenuItem item = new JMenuItem(text);
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
        return item;
    }

    /* MENU ACTIONS */

    //NEU
    private void newBodyModel() {
        //1. Choose File
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Bilddatei auswählen");
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if(f.isDirectory()) return true;
                String filename=f.getName().toLowerCase();
                if(filename.endsWith(".gif")) return true;
                if(filename.endsWith(".png")) return true;
                if(filename.endsWith(".bmp")) return true;
                if(filename.endsWith(".jpg")) return true;
                if(filename.endsWith(".jpeg")) return true;
                return false;
            }

            @Override
            public String getDescription() {
                return "Unterstützte Bildformate";
            }
        });
        int result = fileChooser.showDialog(this, "Bilddatei verwenden");
        if(result!=JFileChooser.APPROVE_OPTION) return;

        File imageFile = fileChooser.getSelectedFile();

        //2. Choose Handling
        ImageProcessingDialog imageProcessingDialog = new ImageProcessingDialog(this, imageFile);
        imageProcessingDialog.setVisible(true);


        Raum processing_result = imageProcessingDialog.getResult();
        if(processing_result == null) return;

        BodyModel bodyModel = BodyModel.createEmptyBodyModel(processing_result);

        tabPane.newEditTab(imageFile.getName(), bodyModel);

    }

}
