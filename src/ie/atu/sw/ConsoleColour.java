package ie.atu.sw;

public class ConsoleColour {
	 
	/*
	 *  Constants for ANSI escape codes to format console output.
	 *  
	 *  Source from the Class Notes (Source code).
	 */
	 public static final String  GREEN_BOLD = "\033[1;32m"; //Green bold text
	 public static final String RESET= "\033[0m"; // Reset text formatting
	 
	 // Fields to store the description and the colour code
	 private final String description;
	 private final String colour;
		
	    /*
	     * Constructor to initialize the ConsoleColour object with a description and colour code.
	     * @parameter "description" - A description of the colour.
	     * @parameter "colour" - The ANSI escape code representing the colour.
	     */
		ConsoleColour(String description, String colour) {
			this.description = description;
			this.colour = colour;
		}
		
		/*
		 * Retrieves the description of the colour.
		 * @return A string representing the description of the colour.
		 */
		public String description() { 
			return this.description; 
		}
		
		/*
		 * Retrieves the formatted colour code.
		 * @return A string representing the colour code.
		 */
		public String colour() { 
			return toString(); // Returns the formatted colour string.
		}
		
		/*
		 *  Returns a string that applies the colour formatting colour to the console output.
		 *  @return A string with ANSI escape codes for bold green colour and reset.
		 */
		@Override
	    public String toString() {
	        return GREEN_BOLD + this.colour + RESET; 
	    }
	 

}
