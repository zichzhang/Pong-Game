package ppPackage;
import ppPackage.ppPaddle;
import ppPackage.ppSimPaddleAgent;
import static ppPackage.ppSimParams.*;
import acm.graphics.*;
import acm.util.RandomGenerator;

import java.awt.Color;

/**
 * The class ppBall will provide an instance of a ball moving
 * under the effects of Newtonian physics. The class has a
 * Thread extension which will cause each instance to run 
 * concurrently with the other methods of the program, i.e
 * ppPaddle, allowing both the ball and the paddle to move.
 */
public class ppBall extends Thread {
	
	// Instance variables
	private double Xinit; // Initial position of ball - X
	private double Yinit; // Initial position of ball - Y
	private double Y; 
	private double Yo;
	private double Vo; // Initial velocity (Magnitude)
	private double theta; // Initial direction
	private double loss; // Energy loss on collision
	private Color color; // Color of ball
	private ppTable table; // Instance of ping-pong table
	private ppPaddle myPaddle; // Instance of ping-pong paddle
	private ppPaddle theAgent;  // Instance of agent paddle
	private double YBall; // Y position of the ball
	boolean traceOn; // enables trace points
	boolean hasEnergy; // checks if simulation is still running
	GOval myBall; // Graphics object representing ball
	GOval trace; // Trace of ball
	
	/**
	* The constructor for the ppBall class copies parameters to instance variables, creates an
	* instance of a GOval to represent the ping-pong ball, and adds it to the display.
	*
	* @param Xinit - starting position of the ball X (meters)
	* @param Yinit - starting position of the ball Y (meters)
	* @param Vo - initial velocity (meters/second)
	* @param theta - initial angle to the horizontal (degrees)
	* @param color - ball color (Color)
	* @param loss - loss on collision ([0,1])
	* @param table - a reference to the ppTable class used to manage the display
	* @param traceOn - enables trace points when true (boolean) 
	*/
	public ppBall(double Xinit, double Yinit, double Vo, double theta, Color color, double
			loss, ppTable table,  boolean traceOn) {
		
		this.Xinit = Xinit; // Copy constructor parameters to instance variables
		this.Yinit = Yinit;
		this.Vo = Vo;
		this.theta = theta;
		this.color = color;
		this.loss = loss;
		this.table = table;
		this.traceOn = traceOn;

		// Create new ball that takes random parameters generated from ppSimPaddleAgent class
		myBall = new GOval(Xinit*SCALE, Yinit*SCALE, 2*bSize*SCALE, 2*bSize*SCALE); 
		myBall.setFilled(true);
		myBall.setColor(color);
		table.getDisplay().add(myBall);
	}
	
	// Run simulation for ball 
	public void run() {
		
		// Initialize parameters
		double Vox=Vo*Math.cos(theta*Pi/180);  // Initial velocity (in X and Y)
		double Voy=Vo*Math.sin(theta*Pi/180);
		
		double time = 0; // time (reset at each interval)
		double Vt = bMass*g/(4*Pi*bSize*bSize*k); // Terminal velocity
		double KEx=ETHR,KEy=ETHR, PE; // Kinetic energy in X and Y directions
		double Xo, X, Vx; // X position and velocity variables
		double Vy; // Y position and velocity variables
		
		Xo = XLWALL+bSize; // Initial X offset
		Yo = Yinit; // Initial Y offset
		
		// While loop 
		hasEnergy = true;
		while (hasEnergy) {
			// Update parameters X, Y, Vx, Vy
			X = Vox*Vt/g*(1-Math.exp(-g*time/Vt)); 
			Y = Vt/g*(Voy+Vt)*(1-Math.exp(-g*time/Vt))-Vt*time;
			Vx = Vox*Math.exp(-g*time/Vt);
			Vy = (Voy+Vt)*Math.exp(-g*time/Vt)-Vt;
			
			// Print values of time, position in x, position in y, velocity in x, and velocity in y, at current loop cycle
			if (TEST) {
				System.out.printf("t: %.2f X: %.2f Y: %.2f Vx: %.2f Vy:%.2f\n",time,Xo+X,Yo+Y,Vx,Vy);
			}
			
			// Check for ball collision with ground
			if (Vy < 0 && (Yo + Y) <= bSize) { 
				
				KEx = 0.5*bMass*Vx*Vx*(1-loss);
				KEy = 0.5*bMass*Vy*Vy*(1-loss);
				PE = 0;
				
				// Calculates for initial velocity for x and y
				Vox = Math.sqrt(2*KEx/bMass);
				Voy = Math.sqrt(2*KEy/bMass);
				
				// Adds a negative sign to initial velocity in x if ball is moving to the left 
				if (Vx < 0) Vox= -Vox;
				
				// Reinitialize parameters
				time = 0; // time is reset at every collision
				Xo += X; // need to accumulate distance between collisions
				Yo = bSize; // the absolute position of the ball on the ground
				X = 0; // (X,Y) is the instantaneous position along an arc,
				Y = 0; // Absolute position is (Xo+X,Yo+Y)
				
				// Break loop => Ball stops if there is no energy left
				if ((KEx+KEy+PE) < ETHR) hasEnergy = false;
				
			}
			// Check for ball collision with right paddle
			if (Vx > 0 && (Xo+X) > myPaddle.getX()+bSize-ppPaddleW/2) {
				if (myPaddle.contact(Xo+X, Yo+Y)==true) { // If there is contact between the ball and the paddle
					
					KEx = 0.5*bMass*Vx*Vx*(1-loss);
					KEy = 0.5*bMass*Vy*Vy*(1-loss);
					PE = bMass*g*Y;
					
					Vox = -Math.sqrt(2*KEx/bMass)*ppPaddleXgain; // Scale X component of velocity
					Voy = Math.sqrt(2*KEy/bMass)*ppPaddleYgain*myPaddle.getSgnVy(); // Scale Y + same dir. as paddle; 
					
					
					// Adds a negative sign to initial velocity in y if ball is falling 
					if (Vy < 0) Voy = -Voy;
					
					// Reinitialize parameters
					time = 0; // time is reset at every collision
					Xo = ppPaddleXinit-bSize; // need to accumulate distance between collisions
					Yo += Y; // the absolute position of the ball on the ground
					X = 0; // (X,Y) is the instantaneous position along an arc,
					Y = 0; // Absolute position is (Xo+X,Yo+Y)   
					
					// Break loop => Ball stops if there is no energy left
					if ((KEx+KEy+PE) < ETHR) hasEnergy = false;
				}
				else {
					hasEnergy = false;
					//Increments the agent score by 1 if human paddle misses the ball
					table.getDisplay().agentPoints.setValue(table.getDisplay().incrementAgentScore());
					break; // Simulation stops if ball does not get contact with the paddle
				}
			}
			
			// Check for ball collision with left agent paddle
			if (Vx < 0 && (Xo + X) <= theAgent.getX()+bSize+ppPaddleW/2) {
				if (theAgent.contact(Xo+X, Yo+Y)==true) { // If there is contact between the ball and the paddle
					KEx = 0.5*bMass*Vx*Vx*(1-loss);
					KEy = 0.5*bMass*Vy*Vy*(1-loss);
					PE = bMass*g*Y;
					
					Vox = Math.sqrt(2*KEx/bMass)*ppAgentXgain; // Scale X component of velocity
					Voy = - Math.sqrt(2*KEy/bMass)*ppAgentYgain*theAgent.getSgnVy(); // Scale Y + same dir. as paddle;
					
					// Adds a negative sign to initial velocity in y if ball is falling 
					if (Vy < 0) Voy = -Voy;
					
					// Reinitialize parameters
					time = 0;
					Xo = XLWALL+bSize;
					Yo += Y;
					X = 0; 
					Y = 0; 
					
					// Break loop => Ball stops if there is no energy left
					if ((KEx+KEy+PE) < ETHR) hasEnergy = false;
				}
				else {
					hasEnergy = false;
					//Increments the human score by 1 if agent paddle misses the ball
					table.getDisplay().humanPoints.setValue(table.getDisplay().incrementHumanScore()); 
					break;
				}
			}
			
			// Update position of the ball on the screen
			int ScrX = (int)((Xo+X-bSize)*SCALE);
			int ScrY = (int)(scrHEIGHT-(Yo+Y+bSize)*SCALE);
			myBall.setLocation(ScrX,ScrY); // Screen units
			
			// Check collision with upper boundary - out of bounds hit by human
			if (myBall.getY() < 0 && Vx < 0) {
				hasEnergy = false;
				table.getDisplay().agentPoints.setValue(table.getDisplay().incrementAgentScore()); // Agent score increment (by 1)
			}
			
			// Check collision with upper boundary - out of bounds hit by agent
			if (myBall.getY() < 0 && Vx > 0) {
				hasEnergy = false;
				table.getDisplay().humanPoints.setValue(table.getDisplay().incrementHumanScore());  // Human score increment (by 1)
			}
			
			
			// Trace trajectory of the ball
			if (traceOn) {
				GOval trace = new GOval(ScrX+(bSize*SCALE),ScrY+(bSize*SCALE),PD,PD);
				trace.setColor(Color.black);
				table.getDisplay().add(trace); 
			}
			table.getDisplay().pause(TICK*TIMESCALE/2);				
			
			time += TICK; // Increment time
			
		}	// While loop ends	
		hasEnergy = false; // No more energy when while loop ends
	}
	
	/**
	* Gets the GObject ball created in the ppBall class.
	* @return graphics object representing ball, called myBall.
	*/
	public GObject getBall() {
		return myBall;
	}
	
	/**
	 * Gets the Y position of the ball
	 * @return Yo + Y
	 */
	public double getYBall() {
		return Yo+Y;
	}
	
	/**
	 * Gets GObject trace created in the ppBall class.
	 * @return graphics object representing the trace, called trace.
	 */
	public GObject getTrace() {
		return trace;
	}
	
	/**
	 * Sets the paddle object
	 * @param myPaddle
	 */
	public void setPaddle (ppPaddle myPaddle) {
		this.myPaddle = myPaddle;
	}
	/**
	 * Sets the agent paddle object
	 * @param theAgent
	 */
	public void setAgent (ppPaddleAgent theAgent) {
		this.theAgent = theAgent;
	}
	/**
	 * If the simulation is running, return true (hasEnergy = true)
	 * @return hasEnergy = true
	 */
	public boolean ballInPlay() {
		return hasEnergy;
	}
	
}
