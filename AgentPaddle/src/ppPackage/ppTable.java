package ppPackage;
import ppPackage.ppSimPaddleAgent;
import static ppPackage.ppSimParams.*;
import acm.graphics.*;
import java.awt.Color;

/**
 * The ppTable class creates a table containing graphics display
 * information like the ground, the left and right wall, and gives
 * its access to the ppSim class. It exports other utility methods 
 * about graphics display.
 */
public class ppTable {

	private ppSimPaddleAgent dispRef; // Instance variable
	
	public ppTable(ppSimPaddleAgent dispRef) {
		
		this.dispRef = dispRef;
		
		// Plot the lines for ground, right wall, and left wall
		GLine groundLine = new GLine(0,scrHEIGHT,scrWIDTH+OFFSET,scrHEIGHT);
		groundLine.setColor(Color.black);
		dispRef.add(groundLine);

	}
	
	/**
	 * Returns the world X value in screen X value by multiplying
	 * by the SCALE factor.
	 * 
	 * @param X - world value of X
	 * @return screen value of X
	 */
	public double toScrX(double X) { 
		return X*SCALE;
	} 
	
	/**
	 * Returns the world Y value in screen Y value by multiplying
	 * by the SCALE factor.
	 * 
	 * @param Y - world value of Y
	 * @return screen value of Y
	 */
	public double toScrY(double Y) { 
		return scrHEIGHT - Y*SCALE;
	} 
	
	/**
	 * Returns screen X value in world X value by dividing
	 * by the SCALE factor.
	 * 
	 * @param ScrX - screen value of X
	 * @return world value of X
	 */
	public double ScrtoX(double ScrX) {
		return ScrX/SCALE;
	}
	
	/**
	 * Returns screen Y value in world X value by dividing
	 * by the SCALE factor.
	 * 
	 * @param ScrY - screen value of Y
	 * @return world value of Y
	 */
	public double ScrtoY(double ScrY) {
		return (scrHEIGHT-ScrY)/SCALE;
	}
	
	/**
	 * Returns a reference to the object that exports the graphics methods used by this program.
	 * @return dispRef
	 */
	public ppSimPaddleAgent getDisplay() { 
		return dispRef;
	}
	/**
	 * Clears the screen when new serve is requested and creates a new instance of myTable.
	 */
	void newScreen() { 
		dispRef.removeAll();
		ppTable myTable = new ppTable(dispRef);
	}	
	
}


