package ppPackage;
import ppPackage.ppTable;
import static ppPackage.ppSimParams.*;

import java.awt.Color;

import acm.graphics.GRect;

/**
 * The class ppPaddle creates an instance of a paddle and 
 * exports methods for interacting with the paddle instance. 
 * This class has a Thread extension which will allow it to 
 * run concurrently with the other methods of the program,
 * i.e. ppBall, allowing both the paddle and the ball to move.
 */
public class ppPaddle extends Thread{
	
	// Instance variables
	private double X;
	private double Y;
	private double Vx, Vy;
	private double lastX, lastY;
	private ppTable myTable;
	private Color myColor;
	private GRect myPaddle;
	
	public ppPaddle(double X, double Y, Color myColor, ppTable myTable) {
		/**
		* The constructor for the ppPaddle class copies parameters to instance variables, creates an
		* instance of a GRect to represent the ping-pong paddle, and adds it to the display.
		*
		* @param X - starting position of the paddle X (meters)
		* @param Y - starting position of the paddle Y (meters)
		* @param myColor - color of the paddle 
		* @param myTable - a reference to the ppTable class used to manage the display
		*/
		this.X = X;
		this.Y = Y;
		this.myColor = myColor;
		this.myTable = myTable;
		
		// Create a GRect object for the paddle
		myPaddle = new GRect(ppPaddleXinit*SCALE,ppPaddleYinit*SCALE,ppPaddleW*SCALE,ppPaddleH*SCALE);
		myPaddle.setFilled(true);
		myPaddle.setColor(myColor);
		myTable.getDisplay().add(myPaddle); // Add paddle to the display
		
	}
	/**
	 * The run method continually updates paddle velocity in response to changes in mouse position
	 */
	public void run() {
		while (true) {
			Vx=(X-lastX)/TICK;
			Vy=(Y-lastY)/TICK;
			lastX=X;
			lastY=Y;
			myTable.getDisplay().pause(TICK*TIMESCALE); // Time to mS (slows down the simulation)
		}
	}
	
	/**
	 * Gets the velocity of the paddle in the X direction 
	 * (which will be 0 here as X is fixed).
	 * @return velocity of the paddle in the X
	 */
	public double getVx() {
		return Vx;
	}
	
	/**
	 * Gets the velocity of the paddle in the Y direction.
	 * @return velocity of the paddle in Y
	 */
	public double getVy() {
		return Vy;
	}
	
	/**
	 * Sets the X position of the paddle and its location in screen value.
	 * @param X position of the paddle
	 */
	public void setX(double X) { //
		this.X = X;
		myPaddle.setLocation(myTable.toScrX(X), myTable.toScrY(Y));
	}	
	
	/**
	 * Sets the Y position of the paddle and its location in screen value.
	 * @param Y position of the paddle
	 */
	public void setY(double Y) {
		this.Y = Y;
		myPaddle.setLocation(myTable.toScrX(X-ppPaddleW/2), myTable.toScrY(Y+ppPaddleH/2));
	}
	
	/**
	 * Gets the X position of the paddle.
	 * @return X position of the paddle
	 */
	public double getX() {
		return X;
	}
	
	/**
	 * Gets the Y position of the paddle.
	 * @return Y position of the paddle
	 */
	public double getY() {
		return Y;
	}

	/**
	 * This method is a multiplying factor and returns -1 if the velocity of the paddle is negative
	 * and +1 if the velocity of the paddle is positive.
	 * @return -1 if Vy is negative and 1 if Vy is positive
	 */
	public double getSgnVy() {
		if (Vy < 1) { 
			return -1;
		}
		else {
			return 1;
		}
	}
	
	/**
	 * This method looks for contact between the ball and the paddle, returns true if contact was
	 * detected and false otherwise.
	 * @param surface at position X, called Sx
	 * @param surface at position Y, called Sy
	 * @return true if there is contact between ball and paddle and false otherwise
	 */
	public boolean contact (double Sx, double Sy) {
		if (Sy >= Y-ppPaddleH/2 && Sy <= Y+ppPaddleH/2) {
			return true;
		}
		else {
			return false;
		}
	}
}
