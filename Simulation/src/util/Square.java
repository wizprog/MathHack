package util;
import javax.swing.JFrame;

public class Square 
{

	public static void main(String args[]) 
{
    // Set width and height of frame
    int frameWidth = 1024;
    int frameHeight = 768;

    // Create new frame and set size
    JFrame frmMain = new JFrame();
    frmMain.setSize(frameWidth, frameHeight);

    // Create a moving square and add to the frame
    MovingSquare mySquare = new MovingSquare(frameWidth, frameHeight);      
    frmMain.add(mySquare);

    // Final configuration settings for frame.
    frmMain.setVisible(true);
    frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frmMain.setTitle("Moving Square");
}

}