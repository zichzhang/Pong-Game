package ppPackage;

import static ppPackage.ppSimParams.*;

import java.awt.Color;

import acm.graphics.GRect;

/**
 * The class ppPaddleAgent creates an instance of a paddle and 
 * adjusts the Y position of the agent paddle with relation to the
 * ball. This class extends ppPaddle, which inherits all methods 
 * of the ppPaddle class.
 */

public class ppPaddleAgent extends ppPaddle {
	
	// Instance variables
	private double X;
	private double Y;
	private double Vx, Vy;
	private double lastX, lastY;
	ppTable myTable;
	ppBall myBall;
	Color myColor;
	GRect myPaddle;
	GRect myPaddleAgent;
	
	public ppPaddleAgent(double X, double Y, Color myColor, ppTable myTable) {
		super(X, Y, myColor, myTable);  
		/**
		 * The constructor for ppPaddleAgent is identical to that of ppPaddle.
		 * @param X - starting position of the paddle X (meters)
		 * @param Y - starting position of the paddle Y (meters)
		 * @param myColor - color of the paddle
		 * @param myTable - a reference to the ppTable class used to manage the display
		 */
		this.myTable = myTable;
	}
	/**
	 * The run method continually updates paddle velocity in response to changes in ball Y position
	 */
	public void run() {
		while (true) {
			Vx=(X-lastX)/TICK;
			Vy=(Y-lastY)/TICK;
			lastX=X;
			lastY=Y;
			setY(myBall.getYBall()); // Make Y position of the agent paddle equal to Y position of the ball
			myTable.getDisplay().pause(TICK*TIMESCALE); // Time to mS (slows down the simulation)
		}
	}
	
	/**
	 * Sets the value of the myBall instance variable in ppPaddleAgent.
	 * @param myBall
	 */
	public void attachBall(ppBall myBall) {
		this.myBall = myBall;
	}
	/**
	 * Gets a reference of the agent paddle object
	 * @return a reference of the agent paddle object
	 */
	public GRect getPaddleAgent() {
		return myPaddleAgent;
	}

}
	