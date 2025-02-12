package ie.atu.sw;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileHandler {
	
	/*
	 * Reads word embeddings from a specified file and store them in a 2D array.
	 * @parameter "filePath" - THe path to the file containing word embeddings.
	 * @throws "IOException" - If an Input/Output error occurs while reading the file.
	 * @return A 2D array where each row contains a word followed by its embedding values. 
	 * 
	 * Source from https://www.youtube.com/watch?v=lHFlAYaNfdo  AND https://www.youtube.com/watch?v=M8xFzcWiORE
	 * AND CLASS NOTES (Source code)
	 * 
	 * Modified to do the following code.
	 */
    public String[][] readEmbeddings(String filePath) throws IOException {
    	
    	// Initialize Bufferedreader to read from the specified embeddings file
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        
     // Array to store words and their embeddings
     String[][] data = new String[59602][51]; // 59602 rows, 51 columns (1 for word, 50 for features)
        int rowIndex = 0; // Index to track the current row in the data array


        while ((line = reader.readLine()) != null) {   // Read the file line by line
        	
        	// Split each line by spaces and store the resulting array in data[rowIndex]
            data[rowIndex] = line.split(" ");
            rowIndex++; // Move to the next row for the next line
        }

        reader.close();
        return data;  // Return the data array
    }
}
