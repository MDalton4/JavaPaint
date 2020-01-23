package daltonfinal;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;

public class Canvas extends JComponent implements Drawable{

    private Image img, defaultImg;  //images fields
    private BufferedImage userImage;    //uploaded image
    private Graphics2D g2d; //graphics 2d field
    private int currentXPos, currentYPos, xPos, yPos;   //ints for mouse position
    private Color curCol = Color.BLACK, curBGColor = Color.WHITE;   //default colors and current colors
    private int curStroke = 1, type;    //default stroke and current types
    private String strokeType = "Default";  //default stroke type is a line or default
    private Stack<BufferedImage> undoStack = new Stack<>(); //undo stack
    private Stack<BufferedImage> redoStack = new Stack<>(); //redo stack

    //canvas constructor
    //sets the canvas defaults and creates mouse listeners
    public Canvas() {
        //set stroke type to 1
        type = 1;
        //set double buffered false and add mouse listener
        setDoubleBuffered(false);
        addMouseListener(new MouseAdapter() {
            @Override
            //when mouse is pressed
            public void mousePressed(MouseEvent e) {
                //add the canvas img to the stack
                saveToUndoStack(img);
                //set the x, y cords
                xPos = (int) e.getX();
                yPos = (int) e.getY();
            }
            //when mouse is released
            @Override
            public void mouseReleased(MouseEvent e) {
                //if the stroke type is 2 draw a rectangle
                if(type == 2) {
                    g2d.drawRect(xPos, yPos, (currentXPos-xPos), (currentYPos-yPos));
                }
                //if its type 3 draw an oval
                else if(type == 3) {
                    g2d.drawOval(xPos, yPos, (currentXPos-xPos), (currentYPos-yPos));
                }
                //repaint the canvas
               repaint();
           }
        });
        //add a mouse motion listener
        addMouseMotionListener(new MouseMotionAdapter() {
            //mouse dragged
            @Override
            public void mouseDragged(MouseEvent e) {
                //set the x, y pos to the end mouse loc
                currentXPos = (int) e.getX();
                currentYPos = (int) e.getY();
                //if its not a null canvas and type is 1 draw a line
                if(g2d !=null && type == 1) {
                   g2d.drawLine(xPos, yPos, currentXPos, currentYPos);
                   //repaint and reset x and y pos
                   repaint();
                   xPos = currentXPos;
                   yPos = currentYPos;
                }
            }
        });
        //set the default image to img (blank canvas)
        defaultImg = img;
    }

    //paint component method
    //allows the canvas to be drawn on
    @Override
    protected void paintComponent(Graphics g) {
        //calls super to create new paint component for the canvas graphics
        super.paintComponent(g);
        //if the img is null
        //create a new image
        if(img == null) {
            //set the size of the image
            img = createImage(getSize().width, getSize().height);
            //create the graphics of the image
            g2d = (Graphics2D) img.getGraphics();
            //set the rendering hint of the image
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            //clear the canvas
            clearCanvas();
        }
        //if the user image isnt null meaning they uploaded or used undo
        if(userImage != null) {
            //create graphics
            userImage.createGraphics();
            //get the graphics
            g2d = (Graphics2D) userImage.getGraphics();
            //set the rendering hint for the graphics
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            //repaint and set the colors
            repaint();
            g2d.setPaint(curCol);
            g2d.setStroke(new BasicStroke(curStroke));
            g.drawImage(userImage, 0, 0, null);
        }
        if(img != null) {
            //draw the image
            g.drawImage(img, 0, 0, null);
        }
    }

    //clear canvas method
    //clears the canvas, resets it to a blank white canvas
    public void clearCanvas() {
        //if there is an image remove it
        if(userImage != null)
            removeImg();
        //set the paint to white, draw a rectangle that is white and repaint
        g2d.setPaint(Color.WHITE);
        g2d.clearRect(0, 0, getSize().width, getSize().height);
        g2d.fillRect(0, 0, getSize().width, getSize().height);
        setBackground(Color.WHITE);
        g2d.setPaint(Color.BLACK);
        repaint();
    }

    //set color method
    //set paint and color to c
    public void setColor(Color c) {
        //set current color and graphics paint color
        curCol = c;
        g2d.setPaint(c);
    }

    //get color method
    //returns the color of the graphics
    public Color getColor() {
        return curCol;
    }

    //set stroke override method
    //sets the stroke to int type passed in
    @Override
    public void setStroke(int s) {
        //if default
        if(strokeType.equals("Default")) {
            //set stroke to normal or line
            g2d.setStroke(normal());
        }
        //else if dotted
        else if(strokeType.equals("Dotted")) {
            //set stroke to dotted line
            g2d.setStroke(dotted());
        }
        //else if dashed
        else if(strokeType.equals("Dashed")) {
            //set stroke to a dashed line
            g2d.setStroke(dashed());
        }
        //set current stroke to s
        curStroke = s;
    }

    //set stroke type override method
    //sets the type
    @Override
    public void setStrokeType(String type) {
        if(type.equals("Default")) {
            g2d.setStroke(normal());
            strokeType = "Default";
        }
        else if(type.equals("Dotted")) {
            g2d.setStroke(dotted());
            strokeType = "Dotted";
        }
        else if(type.equals("Dashed")) {
            g2d.setStroke(dashed());
            strokeType = "Dashed";
        }
    }

    //normal stroke method
    //sets stroke to a line with the correct size
    //returns stroke
    public BasicStroke normal() {
        return new BasicStroke(curStroke);
    }

    //dotted stroke method
    //sets stroke to a dotted line
    //returns stroke
    public BasicStroke dotted() {
        return new BasicStroke(curStroke, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f,
                new float[] {0.1f, 5.0f}, 1);
    }

    //dashed stroke method
    //sets stroke to a dashed line
    //returns line
    //* doesnt work above a size 3 *\\
    public BasicStroke dashed() {
        return new BasicStroke(curStroke, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f,
                new float[] {3.0f, 3.0f}, 1);
    }

    //set type method
    //sets stroke type
    public void setType(int t) {
        type = t;
    }

    //set background override
    //takes in a color to set
    @Override
    public void setBackground(Color c) {
        //sets color and current bg color
        g2d.setColor(c);
        curBGColor = c;
        //draws the rectange
        g2d.fillRect(0, 0, getSize().width, getSize().height);
        //resets paint color to current and repaints
        g2d.setPaint(curCol);
        repaint();
    }

    //override get background  method
    //returns current bg color
    @Override
    public Color getBackground() {
        return curBGColor;
    }

    //get image method
    //returns the image as a buffered image
    public BufferedImage getImg() {
        return (BufferedImage) img;
    }

    //set image method
    //takes a buffered image
    //sets the img and userImage
    public void setImg(BufferedImage i) {
        userImage = i;
        img = i;
        repaint();
    }

    //remove image method
    //removes the user image
    //sets the default image and repaints
    public void removeImg() {
        img = defaultImg;
        userImage = null;
        repaint();
    }

    //copy image method
    //takes in an image
    //returns a copied version
    public BufferedImage copyImage(Image i) {
        //create new image
        BufferedImage imgCopy = new BufferedImage(getSize().width, getSize().height,
            BufferedImage.TYPE_INT_ARGB);
        //create graphics
        Graphics g = imgCopy.createGraphics();
        //draw the image and return image
        g.drawImage(i, 0, 0, getWidth(), getHeight(), null);
        g.dispose();
        return imgCopy;
    }

    //save to undo stack method
    //pushed copied image to stack so it can be undone
    public void saveToUndoStack(Image i) {
        undoStack.push(copyImage(i));
    }

    //undo method
    //checks if the stack is empty
    //pops the undo stack and sets image, puts image on redo stack
    public void undo() {
        if(!undoStack.isEmpty()) {
            BufferedImage im = undoStack.pop();
            redoStack.push(im);
            setImg(im);
            repaint();
        }
        curBGColor = this.getBackground();
    }

    //redo method
    //checks if the stack is empty
    //pops undo stack sets the image, puts image on undo stack
    public void redo() {
        if(!redoStack.isEmpty()) {
            BufferedImage im = redoStack.pop();
            undoStack.push(im);
            setImg(im);
            repaint();
        }
    }

}
