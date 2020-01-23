/*
Mark Dalton
CSC 2560
Due 12/15/17
Fianl project
A paint like gui application
This app allows a user to paint on a canvas and allows them to freely paint with
a few tools at their hands

***Undo / redo works on my mac/ if you undo and save your image it is blank***
*/

package daltonfinal;

public class DaltonFinal {

    public static void main(String[] args) {

        GUI gui = new GUI();
        // Size of the window, in pixels
        gui.setSize(1000, 800);
        // Make the window "visible"
        gui.setVisible(true);

    }
    
}
