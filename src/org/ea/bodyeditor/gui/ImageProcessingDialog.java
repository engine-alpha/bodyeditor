package org.ea.bodyeditor.gui;

import ea.raum.Animation;
import ea.raum.Bild;
import ea.raum.Raum;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Dieser Dialog bekommt ein Bild und wandelt es in die korrekte Form von Raum-Objekt um (Animation vs. Bild)
 * Created by Michael on 09.08.2017.
 */
public class ImageProcessingDialog
extends JDialog {

    private static final Font DIALOG_FONT = new Font("Sans-Serif", Font.PLAIN, 25);

    private final File file;

    private Raum result=null;

    private JSpinner spritesheetRows, spritesheetCols;
    private JRadioButton simpleOption;
    private JRadioButton spriteOption;

    public ImageProcessingDialog(Frame frame, File file) {
        super(frame);
        this.setTitle("Bild-Einlade-Optionen");
        this.file = file;

        setModal(true);
        Point loc = frame.getLocationOnScreen();
        setLocation(loc.x + 50, loc.y + 50);

        setupContent();

        setPreferredSize(new Dimension(500, 300));

        pack();
    }


    private void setupContent() {
        getContentPane().setLayout(new BorderLayout());

        ButtonGroup imageOptions = new ButtonGroup();

        //Radio Buttons
        JPanel radiobuttons = new JPanel();
        radiobuttons.setLayout(new BoxLayout(radiobuttons, BoxLayout.PAGE_AXIS));
        simpleOption = new JRadioButton("Als Einzelbild einladen");
        spriteOption = new JRadioButton("Als Spritesheet einladen");
        simpleOption.setFont(DIALOG_FONT);
        spriteOption.setFont(DIALOG_FONT);
        imageOptions.add(simpleOption);
        imageOptions.add(spriteOption);

        radiobuttons.add(simpleOption, Component.LEFT_ALIGNMENT);
        radiobuttons.add(spriteOption, Component.LEFT_ALIGNMENT);

        getContentPane().add(radiobuttons, BorderLayout.NORTH);


        //Combo Box
        //String[] imageOptions = new String[] {"Als Einzelbild einladen", "Als Spritesheet einladen"};
        //JComboBox options;

        //Spinners
        JPanel spinners = new JPanel();
        ///spinners.setPreferredSize(new Dimension(500, 50));

        spritesheetRows = new JSpinner();
        spritesheetRows.setModel(createRowColNumberModel());
        spritesheetCols = new JSpinner();
        spritesheetCols.setModel(createRowColNumberModel());

        spinners.setLayout(new GridLayout(2,2));
        JLabel t1 = new JLabel("Spritesheet-Zeilen");
        t1.setFont(DIALOG_FONT);
        spinners.add(t1);
        spinners.add(spritesheetRows);
        JLabel t2 = new JLabel("Spritesheet-Spalten");
        t2.setFont(DIALOG_FONT);
        spinners.add(t2);
        spinners.add(spritesheetCols);

        getContentPane().add(spinners, BorderLayout.CENTER);

        //Buttons
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Abbrechen");

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());

        buttons.add(ok);
        buttons.add(Box.createHorizontalGlue());
        buttons.add(cancel);

        getContentPane().add(buttons,BorderLayout.SOUTH);


        //Listener
        final ActionListener radioButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(simpleOption.isSelected()) {
                    spritesheetRows.setEnabled(false);
                    spritesheetCols.setEnabled(false);
                } else {
                    spritesheetRows.setEnabled(true);
                    spritesheetCols.setEnabled(true);
                }
            }
        };
        simpleOption.addActionListener(radioButtonListener);
        spriteOption.addActionListener(radioButtonListener);
        simpleOption.setSelected(true);
        radioButtonListener.actionPerformed(null);

        ok.addActionListener((e)->finishUp());
        cancel.addActionListener((e -> setVisible(false)));
    }

    private SpinnerNumberModel createRowColNumberModel() {
        SpinnerNumberModel numberModel = new SpinnerNumberModel();
        numberModel.setMinimum(1);
        numberModel.setMaximum(120);
        numberModel.setValue(1);
        return numberModel;
    }

    /**
     * Schließt den Dialog bestätigend ab.
     */
    private void finishUp() {
        if(simpleOption.isSelected()) {
            //Simple Load
            result = new Bild(file.getAbsolutePath());
        } else {
            try {
                result = Animation.createFromSpritesheet(100, file.getAbsolutePath(),
                        (Integer) spritesheetCols.getValue(),
                        (Integer) spritesheetRows.getValue());
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(getParent(),
                        "Die Maße der Bilddatei passen nicht mit der gewählten Rasterung zusammen: "+e.getMessage(),
                        "Fehler in der Bilddatei", JOptionPane.WARNING_MESSAGE);
            }
        }
        setVisible(false);
    }

    /**
     *
     * @return NULL = Dialog abgebrochen, sonst das Eingeladene Raum-Objekt.
     */
    public Raum getResult() {
        return result;
    }
}
