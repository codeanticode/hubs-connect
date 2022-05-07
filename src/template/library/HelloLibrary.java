package template.library;


import processing.core.*;
import avn.portal.*;

import java.net.URL;

/**
 * This is a template class and can be used to start a new processing Library.
 * Make sure you rename this class as well as the name of the example package 'template' 
 * to your own Library naming convention.
 * 
 * (the tag example followed by the name of an example included in folder 'examples' will
 * automatically include the example in the javadoc.)
 *
 * @example Hello 
 */

public class HelloLibrary {
	
	// myParent is a reference to the parent sketch
	PApplet myParent;

	int myVariable = 0;
	
	public final static String VERSION = "##library.prettyVersion##";
	

	/**
	 * a Constructor, usually called in the setup() method in your sketch to
	 * initialize and start the Library.
	 * 
	 * @example Hello
	 * @param theParent the parent PApplet
	 */
	public HelloLibrary(PApplet theParent) {
		myParent = theParent;
		welcome();
	}
	
	
	private void welcome() {
		System.out.println("##library.name## ##library.prettyVersion## by ##author##");
	}
	
	
	public String sayHello() {
		
		String ROOM_ID = "V32UWm4";
		String HUB_AUTH_TOKEN = "AUTH_NOT_REQUIRED_FOR_TEST";

		// Some resources about Kotlin-Java interoperability:
        // https://kotlinlang.org/docs/java-to-kotlin-interop.html
        // https://medium.com/google-developer-experts/from-java-to-kotlin-and-back-i-java-calling-kotlin-9abfc6496b04
		// https://livebook.manning.com/book/the-joy-of-kotlin/a-mixing-kotlin-with-java/v-8/226

		try {
			URL HUB_SERVER = new URL("https://hubs.mozilla.com");
			avn.portal.ReticulumConnection connection = new avn.portal.ReticulumConnection(HUB_SERVER, HUB_AUTH_TOKEN);
			avn.portal.RoomConnection room = new avn.portal.RoomConnection(ROOM_ID, connection, "hubs-connect");
			

			if (room.isOpen()) {
				System.out.println("SUCCESS to open room");
			}

			Thread.sleep(1000);

			room.close();

			System.out.println("SUCCESS to close room");
			
		} catch (Exception ex) {
			System.err.println("Hubs connection failed");
			ex.printStackTrace();
		}


		return "hello library.";
	}
	/**
	 * return the version of the Library.
	 * 
	 * @return String
	 */
	public static String version() {
		return VERSION;
	}

	/**
	 * 
	 * @param theA the width of test
	 * @param theB the height of test
	 */
	public void setVariable(int theA, int theB) {
		myVariable = theA + theB;
	}

	/**
	 * 
	 * @return int
	 */
	public int getVariable() {
		return myVariable;
	}
}

