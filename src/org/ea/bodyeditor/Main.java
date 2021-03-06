package org.ea.bodyeditor;

import org.ea.bodyeditor.gui.BodyEditorFrame;

import javax.swing.*;

/**
 * Main-Class. Only exists to single out the main method (= startup behaviour).
 * Created by Michael on 08.08.2017.
 */
public class Main {

    public static void main(String[] args) {
        //Set correct Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        //Create and set up frame
        BodyEditorFrame frame = BodyEditorFrame.FRAME_INSTANCE;
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocation(200, 200);

        if(args.length != 0) {
            //First Argument = File to load
            //TODO
        }

        //Visualize Frame
        frame.setVisible(true);
    }
}
