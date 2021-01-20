/**
 * COPYRIGHT NOTICE: Parts of the code used in this 
 * program are based on Assignment 4 written by 
 * Professor Frank Ferrie.
 */

package ppPackage;

import static ppPackage.ppSimParams.*;
import ppPackage.ppBall;
import acm.gui.IntField;
import acm.program.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;

import acm.util.RandomGenerator;

/**
 * The ppSimPaddleAgent class incorporates a primitive user interface which allows the user to
 * clear the screen, restart the serve, count the scores, enable/disable trace, control lag, 
 * and quit the simulation. It also generates a ball of random parameters: X initial position
 * (constant), Y initial position, loss coefficient, initial velocity, initial angle, color(RED).
 */
public class ppSimPaddleAgent extends GraphicsProgram {
	
	// Instance variables
	ppTable myTable;
	ppPaddle myPaddle;
	ppPaddleAgent theAgent;
	ppBall myBall;
	JToggleButton trace;
	IntField agentPoints;
	IntField humanPoints;
	public int humanScore = 0;
	public int agentScore = 0;
	
	public void init() {
		this.resize(scrWIDTH+OFFSET,scrHEIGHT+OFFSET);
		
		// Creates buttons for clear, new serve, trace, and quit 
		add(new JButton("Clear"), SOUTH);
		add(new JButton("New Serve"), SOUTH);
		trace = new JToggleButton(("Trace"), true);
		add(trace, SOUTH);
		trace.setActionCommand("Trace");
		add(new JButton("Quit"), SOUTH);

		// User input - asks for user's name
		Scanner scan1 = new Scanner(System.in);
		System.out.println("Enter User's Name: ");
		String userName = scan1.nextLine();
		
		// User input - asks for agent's name
		Scanner scan2 = new Scanner(System.in);
		System.out.println("Enter Agent's Name: ");
		String agentName = scan2.nextLine();
		
		// Sets user's name in the scoreboard
		add(new JLabel(userName), NORTH);
		humanPoints = new IntField();
		add(humanPoints, NORTH);
		
		// Sets agent's name in the scoreboard
		add(new JLabel(agentName), NORTH);
		agentPoints = new IntField();
		add(agentPoints, NORTH);
		
		addMouseListeners(); // Adds a mouse listener
		addActionListeners(); // Adds an action listener
		
		myTable = new ppTable(this); // Create instance of ppTable
		myBall = newBall(); // Instance myBall
		
		myPaddle = new ppPaddle(ppPaddleXinit,ppPaddleYinit,ColorPaddle,myTable); // Create ppPaddle object
		theAgent = new ppPaddleAgent(ppAgentXinit,ppAgentYinit,ColorAgent,myTable); // Create agent ppPaddle object
		
		myBall.setPaddle(myPaddle);
		myBall.setAgent(theAgent);
		theAgent.attachBall(myBall); 
		
		add(myBall.getBall());  
		
		// Each thread must be explicitly started		
		theAgent.start();
		myPaddle.start();
		pause(1000);
		myBall.start();
		
	}
	
	/**
	* Mouse Handler - a moved event moves the paddle up and down in Y.
	*/
	public void mouseMoved(MouseEvent e) {
		myPaddle.setY(myTable.ScrtoY((double)e.getY()));
	}
	
	/**
	 * Button Handler - sets the function for each button. 
	 */
	
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		// Clear button clears the entire display and resets the score
		if (command.equals("Clear")) {
			myTable.newScreen();
			humanScore = 0;
			agentScore = 0;
			humanPoints.setValue(humanScore);
			agentPoints.setValue(agentScore);
		}
		// New Serve button clears the entire display and creates new ball, paddle, and agent paddle instances.
		// If ball is still in play, New Serve button does nothing.
		else if (command.equals("New Serve")) {
			if (myBall.ballInPlay() == true) {
			}
			else {
				myTable.newScreen();
				myTable = new ppTable(this); // Create instance of ppTable
				myBall = newBall(); // Instance myBall
				
				myPaddle = new ppPaddle(ppPaddleXinit,ppPaddleYinit,ColorPaddle,myTable); // Create ppPaddle object
				theAgent = new ppPaddleAgent(ppAgentXinit,ppAgentYinit,ColorAgent,myTable); // Create agent ppPaddle object
				
				myBall.setPaddle(myPaddle);
				myBall.setAgent(theAgent);
				theAgent.attachBall(myBall);
				
				add(myBall.getBall()); 
				
				// Each thread must be explicitly started		
				theAgent.start();
				myPaddle.start();
				pause(1000);
				myBall.start();
			}
		}
		// Quit button exits the simulation
		else if (command.equals("Quit")) {
			System.exit(0);
		}
	}
	
	/**
	 * Generates an instance of a new ball with random parameters X initial position
     *(constant), Y initial position, loss coefficient, initial velocity, initial angle,
     * color(RED).
	 * @return myBall - an instance of a new ball
	 */
	public ppBall newBall() {
		
		// Create instance of RandomGenerator Class
		RandomGenerator rgen = RandomGenerator.getInstance();
		rgen.setSeed(RSEED);
		
		// Generate random parameters for 1 ball 
		double Xinit = XLWALL + bSize; // FIXED initial X position 
		Color iColor = ColorBall; // Color set to RED
		double iYinit = rgen.nextDouble(YinitMIN,YinitMAX); // Random initial Y position
		double iLoss = rgen.nextDouble(EMIN,EMAX); // Random loss coefficient
		double iVel = rgen.nextDouble(VoMIN,VoMAX); // Random initial velocity
		double iTheta = rgen.nextDouble(ThetaMIN,ThetaMAX); // Random initial angle
		
		myBall = new ppBall(XINIT,iYinit,iVel,iTheta,iColor,iLoss,myTable,trace.isSelected()); // Create a new instance of the ball
		
		return myBall;
	
	}
	
	/**
	 * Increments the agent's score by 1
	 * @return agentScore (incremented by 1)
	 */
	public int incrementAgentScore() {
		return agentScore += 1;
	}
	
	/**
	 * Increments the human's score by 1
	 * @return humanScore (incremented by 1)
	 */
	public int incrementHumanScore() {
		return humanScore += 1;
	}
}
