package daltonfinal;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GUI extends JFrame {
    
    Canvas canvas;  //canvas object to paint on
    Color currentColor = Color.BLACK, bgColor = Color.WHITE;    //default color fields
    private ToolButton clearBtn, eraserBtn, colorBtn, bgBtn;    //buttons
    private ToolComboBox strokeBox, shapeBox;   //combo boxes
    private String[] lineTypes = {"Default Line", "Dotted Line", "Dashed Line"};    //line types for combobox
    private String[] shapeTypes = {"Line", "Rectangle", "Oval"};    //shape types for combobox
    private JMenu menuFile, menuEdit;   //menu fields
    private JMenuItem item1, item2, item3, item4, item5, item6; //menu items
    private JPanel toolPanel;   //panel for tools
    private JTextField strokeIn;    //field for stroke size
    
    //constructor for gui
    //initializes and adds objects to gui
    public GUI() {
        //sets name of gui frame
        super("Dalton's Paint");
        //creates border layout
        setLayout(new BorderLayout());
        //initialize canvas and toolpanel
        canvas = new Canvas();
        toolPanel = new JPanel();
        //create the mneu
        createMenu();
        //add the canvas
        add(canvas, BorderLayout.CENTER);
        
        //create clear button
        //add an action listener
        clearBtn = new ToolButton("Clear");
        clearBtn.addActionListener(new ToolListener(clearBtn, canvas));
        
        //create the stroke size text field
        //add a focus listener and action listener
        strokeIn = new JTextField("Stroke Size", 6);
        strokeIn.addFocusListener(new FocusListener() {
            //when gained focus reset text
            @Override
            public void focusGained(FocusEvent e) {
                if(strokeIn.getText().equals("Stroke Size")) {
                    strokeIn.setText("");
                } 
            }
            //when it loses focus set the brush size
            @Override
            public void focusLost(FocusEvent e) {
                if(!strokeIn.getText().equals("Stroke Size")) {
                    //try to set the stroke as an int
                    try {
                        canvas.setStroke(Integer.parseInt(strokeIn.getText()));
                    } 
                    //catch a number format exception for non ints and set it to 1
                    catch(NumberFormatException ex) {
                        canvas.setStroke(1);
                    }
                }
            }
        });
        strokeIn.addActionListener(new ToolListener(strokeIn, canvas));
        
        //create eraser button and add action listener
        eraserBtn = new ToolButton("Eraser");
        eraserBtn.addActionListener(new ToolListener(eraserBtn, canvas));
        
        //create color button and add action listener
        colorBtn = new ToolButton("Brush Color");
        colorBtn.addActionListener(new ToolListener(colorBtn, canvas));
        
        //create background color button and add action listener
        bgBtn = new ToolButton("Background Color");
        bgBtn.addActionListener(new ToolListener(bgBtn, canvas));
        
        //create the stroke type combo box and add action listener
        strokeBox = new ToolComboBox("-Stroke type-", lineTypes);
        strokeBox.addActionListener(new ToolListener(strokeBox, canvas));
        
        //create the shape type combo box and add action listner
        shapeBox = new ToolComboBox("-Shape type-", shapeTypes);
        shapeBox.addActionListener(new ToolListener(shapeBox, canvas));
        
        //add the buttons / boxes to tool pane
        toolPanel.add(clearBtn);
        toolPanel.add(strokeIn);
        toolPanel.add(eraserBtn);
        toolPanel.add(colorBtn);
        toolPanel.add(bgBtn);
        toolPanel.add(strokeBox);
        toolPanel.add(shapeBox);
        //add the toolpanel in the north or top
        add(toolPanel, BorderLayout.NORTH);
        
        //set the close operation
        //make it so it asks the user to save before exiting
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //create dialog box to ask user to save or not
                int confirmed = JOptionPane.showConfirmDialog(null, "Do you wish to save before exiting?",
                        "Closing", JOptionPane.YES_NO_OPTION, 
                        JOptionPane.QUESTION_MESSAGE);
                //if user selects no exit
                if(confirmed == JOptionPane.NO_OPTION)
                    System.exit(0);
                //else save and exit
                else if(confirmed == JOptionPane.YES_OPTION) {
                    save();
                    System.exit(0);
                }
            }
        });
    }//end of gui
    
    //create menu method
    //creates the menu bar at the top
    //allows the user to do menu like options with their art
    private void createMenu() {
        //create menu bars and options on bar
        JMenuBar menuBar = new JMenuBar();
        menuFile = new JMenu("File");
        menuEdit = new JMenu("Edit");
        
        //add a listener for the menu bar
        ActionListener menuListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //if they select new it removes the image
                if(e.getSource() == item1) {
                    canvas.removeImg();
                } //else if they select load, prompt user to load image
                else if(e.getSource() == item2) {
                    load();
                } //else if they select save, prompt the user to save image
                else if(e.getSource() == item3) {
                    save();
                } //else if they select exit close the program
                else if(e.getSource() == item4) {
                    System.exit(0);
                } //else if they select undo, undo the last action
                else if(e.getSource() == item5) {
                    canvas.undo();
                } //else if they select redo, redo the last undo action
                else if(e.getSource() == item6){
                    canvas.redo();
                }
            }
        };
        
        //create the new menu option with listener
        item1 = new JMenuItem("New");
        item1.addActionListener(menuListener);
        
        //create the open option with listener
        item2 = new JMenuItem("Open");
        item2.addActionListener(menuListener);
        
        //create save option with listener for key event
        item3 = new JMenuItem("Save");
        item3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, 
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        item3.addActionListener(menuListener);
        
        //create exit option with listener
        item4 = new JMenuItem("Exit");
        item4.addActionListener(menuListener);
        
        //create undo option with listener for key event
        item5 = new JMenuItem("Undo");
        item5.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        item5.addActionListener(menuListener);
        
        //create redo option with listener for key event
        item6= new JMenuItem("Redo");
        item6.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, 
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        item6.addActionListener(menuListener);
               
        //add the items and seperators
        menuFile.add(item1); menuFile.addSeparator();
        menuFile.add(item2); menuFile.addSeparator();
        menuFile.add(item3); menuFile.addSeparator();
        menuFile.add(item4);
        menuEdit.add(item5); menuEdit.addSeparator();
        menuEdit.add(item6);
        //add the options to the bar
        menuBar.add(menuFile);
        menuBar.add(menuEdit);
        //set the menu bar
        setJMenuBar(menuBar);
    }
    
    //save method
    //prompts the user with a file chooser to save their canvas as a png or jpg file
    //** doesnt seem to work when an undo is used **//
    public void save() {
        //get the system directory
        String dir = System.getProperty("user.home");
        //make the default the users desktop
        JFileChooser chooser = new JFileChooser(dir + "/Desktop");
        //filter for only jpg and png images
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images",
                "jpg", "png");
        //add the filter
        chooser.addChoosableFileFilter(filter);
                    
        //if the dialog opens allow them to save
        if(chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            //saves canvas as image
            BufferedImage outImg = canvas.getImg();
            //try to get the canvas and write it as a png or jpg
            try {
                ImageIO.write(outImg, "png", chooser.getSelectedFile());
                ImageIO.write(outImg, "jpg", chooser.getSelectedFile());
            } 
            //catch IO exception that it coulnt write the image to a file
            catch(IOException ex) {
                    System.out.println(ex);
            }
        }
    }//end of save
    
    //load method
    //prompts the user with a file choose to upload an image the canvas
    public void load() {
        //creates a new image
        BufferedImage image = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);
        //create file chooser
        JFileChooser chooser = new JFileChooser();
        //if chooser opens
        if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            //get the file selected
            File selected = chooser.getSelectedFile();
            //try to read the image and set the canvas
            try {
                image = ImageIO.read(selected);
                canvas.setImg(image);
            } 
            //catch io exception that it couldnt read the image
            catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }
    
}