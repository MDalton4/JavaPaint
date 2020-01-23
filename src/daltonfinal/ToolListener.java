package daltonfinal;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ToolListener implements ActionListener{
    
    private JButton btnName;    //button name field
    private JComboBox boxName;  //box name field
    private JTextField fieldName;   //text field name
    private Canvas canvas;  //canvas
    private Color currentColor, bgColor;    //current colors
    
    //constructor for jbuttons
    //takes in a canvas and initialized fields
    public ToolListener(JButton b, Canvas c) {
        btnName = b;
        canvas = c;
        currentColor = canvas.getColor();
        bgColor = canvas.getBackground();
    }
    
    //constructor for cmobo boxes
    //takes in a canvas and initialized fields
    public ToolListener(JComboBox b, Canvas c) {
        boxName = b;
        canvas = c;
        currentColor = canvas.getColor();
        bgColor = canvas.getBackground();
    }
    
    //constructor for text fields
    //takes in a canvas and initialized fields
    public ToolListener(JTextField f, Canvas c) {
        fieldName = f;
        canvas = c;
        currentColor = canvas.getColor();
        bgColor = canvas.getBackground();
    }

    //action listener for tools
    @Override
    public void actionPerformed(ActionEvent e) {
        //get the source of action
        Object source = e.getSource();
        //if source is a jbutton
        if(source instanceof JButton) {
            //if the name is clear for clear button
            if(btnName.getText().equals("Clear")) {
                //clear canvas
                canvas.clearCanvas();
            }
            //else if its eraser
            else if(btnName.getText().equals("Eraser")) {
                //get the background color and set the eraser to the color
                bgColor = canvas.getBackground();
                canvas.setColor(bgColor);
            }
            //else if its the brush color btn
            else if(btnName.getText().equals("Brush Color")) {
                //open a jcolorchooser
                currentColor = JColorChooser.showDialog(canvas,
                    "Select Brush Color", 
                    currentColor);
                //if the color isnt null
                if(currentColor != null) {
                    //set the color
                    canvas.setColor(currentColor);
                    btnName.setBackground(currentColor);
                }
            }
            //else if btn is background color
            else if(btnName.getText().equals("Background Color")) {
                //open a color chooser
                bgColor = JColorChooser.showDialog(canvas,
                    "Select Brush Color", 
                    canvas.getBackground());
                //if its not null
                if(bgColor != null) {
                    //set the background
                    canvas.setBackground(bgColor);
                    btnName.setBackground(bgColor);
                }
            }
        } 
        //else if instance of combo box
        else if(source instanceof JComboBox) {
            //get the selected object
            Object selected = boxName.getSelectedItem();
            //if its stroke box
            if(boxName.getName().equals("-Stroke type-")) {
                //if its default line
                if(selected.toString().equals("Default Line")) {
                    //set line to line
                    canvas.setStrokeType("Default");
                }
                //else if dotted
                else if(selected.toString().equals("Dotted Line")) {
                    //set line to dotted line
                    canvas.setStrokeType("Dotted");
                }
                //else if dashed
                else if(selected.toString().equals("Dashed Line")) {
                    //set line to dashed
                    canvas.setStrokeType("Dashed");
                }
            }
            //else if its shape box
            else if(boxName.getName().equals("-Shape type-")) {
                //if line
                if(selected.toString().equals("Line")) {
                    //set stroke to a line
                    canvas.setType(1);
                }
                //if rectangle
                else if(selected.toString().equals("Rectangle")) {
                    //set stroke to draw rectangles
                    canvas.setType(2);
                }
                //if oval
                else if(selected.toString().equals("Oval")) {
                    //set stroke to draw ovals
                    canvas.setType(3);
                }
            }
        }
        //else if instance of text field
        else if(source instanceof JTextField) {
            //try set the stroke with an int
            try {
                canvas.setStroke(Integer.parseInt(fieldName.getText()));
                //catch if not int 
            } catch(NumberFormatException ex) {
                System.out.println(ex);
                canvas.setStroke(1);
            }
        }
    }

}
    
