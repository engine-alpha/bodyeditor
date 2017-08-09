package org.ea.bodyeditor.gui;

import ea.raum.Raum;
import org.ea.bodyeditor.gui.tools.Toolbar;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * The Software's main frame. Contains all UI Elements.
 * Created by Michael on 08.08.2017.
 */
public class BodyEditorFrame
extends JFrame {

    /**
     * Software's Frame Title
     */
    public static final String FRAME_TITLE = "Engine Alpha - Body Editor";

    /**
     * Die Toolbar des Editors
     */
    private final Toolbar toolbar;

    private final EditorTabPane tabPane;

    public BodyEditorFrame() {
        super(FRAME_TITLE);

        //Create MenuBar
        JMenuBar jMenu = createEditorMenuBar();
        super.setJMenuBar(jMenu);


        //Create Content
        super.getContentPane().setLayout(new BorderLayout());
        super.getContentPane().add(toolbar=new Toolbar(), BorderLayout.WEST);
        super.getContentPane().add(tabPane=new EditorTabPane(), BorderLayout.CENTER);


        //Dimension Setting
        super.setMinimumSize(new Dimension(400, 400));
        super.setPreferredSize(new Dimension(850, 650));
        pack();
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



        jMenuBar.add(bearbeiten);

        //Ansicht
        JMenu ansicht = new JMenu("Ansicht");

        jMenuBar.add(ansicht);

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
