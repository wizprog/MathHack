package util;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

@SuppressWarnings("serial")
public class MovingSquare extends JPanel implements ActionListener, KeyListener
{
    // We need a timer to move the shape
    Timer shapeTimer = new Timer(5, this);

    // X and Y coordinates of square (top left corner), and the factors by which it will move or resize each time
    double xPos = 0, yPos = 0, movementX = 0, movementY = 0;

    // Size of the square
    int squareSize = 40;

    // Width and height of the parent frame
    int windowWidth;
    int windowHeight;

    // Movement bounds of the square
    // These will prevent it from being moved off the edge of the frame
    int xBound;
    int yBound;

    // Constructor method for our class
    public MovingSquare(int w, int h)   // Constructor is passed the size of the parent frame
    {
        // Start the timer
        shapeTimer.start();

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        windowWidth = w;
        windowHeight = h;

        xBound = (windowWidth - squareSize);
        yBound = (windowHeight - squareSize);
    }

    // This is where the fun starts!  Painting the graphics object
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        g2.setColor(Color.black);

        // Create a new rectangle (which is actually a square!)
         Rectangle2D movableSquare = new Rectangle2D.Double(xPos, yPos, squareSize, squareSize);
         
        g2.fill(movableSquare);
        // Draw the above square on the graphics object
        g2.draw(movableSquare);
    }

    public void actionPerformed(ActionEvent e) 
    {
        // Redraw the square when something happens
        repaint();

        // Set the new x and y coordinates, depending on which direction we have moved
        xPos += movementX;
        yPos += movementY;
    }

    public void moveUp() 
    {
        // Check to see if the shape is already at the top edge of the screen
        if (yPos == 0) 
        {
            movementY = 0;
            movementX = 0;
        }

        // Set the movement factor - negative Y because we are moving UP!
        movementY = -0.5;
        movementX = 0;
    }

    public void moveDown() 
    {
        // Check to see if the shape is already at the bottom edge of the screen - specified by the X and Y bounds
        if (yPos == yBound) 
        {
            movementY = 0;
            movementX = 0;              
        }

        // Set the movement factor - positive Y because we are moving DOWN!
        movementY = 0.5;
        movementX = 0;
    }

    public void moveLeft()
    {
        // Check to see if the shape is already at the left hand edge of the screen         
        if (xPos == 0) 
        {
            movementY = 0;
            movementX = 0;              
        }

        // Set the movement factor - negative X because we are moving LEFT!         
        movementX = -0.5;
        movementY = 0;
    }

    public void moveRight() 
    {
        // Check to see if the shape is already at the right hand edge of the screen - specified by the X and Y bounds
        if (xPos == xBound)
        {
            movementY = 0;
            movementX = 0;
        }

        // Set the movement factor - positive X because we are moving RIGHT!            
        movementX = 0.5;
        movementY = 0;
    }

    public void enlargeSquare() 
    {
        // Make the square larger
        squareSize++;
    }

    public void shrinkSquare() 
    {
        // Make the square smaller
        squareSize--;
    }

    public void keyPrsessed(KeyEvent e) 
    {
        // Get the Key Code of the key that has been pressed
        int keyCode = e.getKeyCode();

        // If the up key has been pressed
        if (keyCode == KeyEvent.VK_UP) 
        {
            // Move shape up
            moveUp();
        }

        // If the down key has been pressed
        if (keyCode == KeyEvent.VK_DOWN) 
        {
            // Move shape down
            moveDown();
        }

        // If the right key is pressed
        if (keyCode == KeyEvent.VK_RIGHT)
        {
            // Move shape right
            moveRight();
        }

        // If the left key is pressed
        if (keyCode == KeyEvent.VK_LEFT) 
        {
            // Move shape left
            moveLeft();
        }

        // If the left brace key is pressed
        if (keyCode == KeyEvent.VK_OPEN_BRACKET)
        {
            shrinkSquare();
        }

        // If the right brace key is pressed
        if (keyCode == KeyEvent.VK_CLOSE_BRACKET)
        {
            enlargeSquare();
        }
    }

    public void keyTyped(KeyEvent e) 
    {

    }

    public void keyReleased(KeyEvent e)
    {
        // Get the Key Code of the key that has been released
        int keyCode = e.getKeyCode();

        // If the down key was released
        if (keyCode == KeyEvent.VK_UP) 
        {
            movementX = 0;
            movementY = 0;
        }

        // If the down key was released
        if (keyCode == KeyEvent.VK_DOWN) 
        {
            movementX = 0;
            movementY = 0;
        }

        // If the right key was released
        if (keyCode == KeyEvent.VK_RIGHT) 
        {
            movementX = 0;
            movementY = 0;
        }

        // If the left key was released
        if (keyCode == KeyEvent.VK_UP) 
        {
            movementX = 0;
            movementY = 0;
        }               
    }

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}   
}