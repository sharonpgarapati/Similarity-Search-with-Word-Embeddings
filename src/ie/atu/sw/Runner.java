package ie.atu.sw;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Runner {

    private String[] words; // Array to store the words from the embeddings file.
    private double[][] embeddings; // 2D array to store the embedding vectors for the words.
    private final static int FEATURE_COUNT = 50; // Define the number of features per embedding
    
    
    // Main method to start the application
    public static void main(String[] args) {
        Runner app = new Runner(); // Create an instance of Runner.
        app.startMenu(); // Start the menu-driven application.
    }
    
   
    // Method to start the application menu.
    public void startMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        
        // Source from Class Notes (Source Code)
        while (running) {   
        	// Display the menu options.
        	System.out.println(ConsoleColour.GREEN_BOLD);
        	System.out.println("************************************************************");
    		System.out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
    		System.out.println("*                                                          *");
    		System.out.println("*          Similarity Search with Word Embeddings          *");
    		System.out.println("*                                                          *");
    		System.out.println("************************************************************");
            System.out.println("\nMenu:");
            System.out.println("(1) Specify Embedding File");
    		System.out.println("(2) Specify an Output File (default: ./output.txt)");
    		System.out.println("(3) Enter a Word or Text");
    		System.out.println("(4) Configure Options");
    		System.out.println("(5) Calculate Distance");
    		System.out.println("(6) Quit");
            System.out.print("Select Option [1-6]> ");
            
            // Get the user's choice
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
             // Switch case to handle different new options
            switch (choice) {
    
            	case 1:
                    System.out.print("Specify Embedding File ");
                    System.out.print("Enter the path to the embeddings file: ");
                    String filePath = scanner.nextLine();
				
				 
				// Load embeddings from the specified file.
                    try {
                        loadEmbeddings(filePath);
                    } catch (IOException e) {
                        System.err.println("Error loading embeddings: " + e.getMessage());
                    }
                    break;
                    
                case 2:
                	// Specify output file path.
                	System.out.println("Specify an Output File (default: ./output.txt)");
                	System.out.print("Enter the output file path: ");
                	String outputFile = scanner.nextLine();
                	outputPath(outputFile.isEmpty()? "filePath" : outputFile);
                	break;
                	
                case 3:   
                	// Enter a word or text for processing
                    System.out.print("Enter a word or text: ");
                    String userInput = scanner.nextLine();
                    processExpression(userInput);
                    
                    System.out.print("Enter the number of similar words to find: ");
                    int n = scanner.nextInt();
                    
				scanner.nextLine();    
                    try {
                        findSimilarWords(userInput, n, outputPath("outPutFile"));
                    } catch (IOException e) {
                        System.err.println("Error finding similar words: " + e.getMessage());
                    }
                    break;
                    
                case 4:     
                	// Configure options for similarity comparison
                	configureOptions(scanner);           
                    
                    break;
                case 5:
                	
                	  // Calculate distance between two words.
                	  System.out.println("Calculate Distance");
                	  System.out.println("Select distance type: ");
                	  System.out.println("1. Cosine ");
                	  System.out.println("2. Dotproduct ");
                	  System.out.println("3. Euclidean");
                	 int distanceType = scanner.nextInt();
                	 scanner.nextLine();
                	  
                      System.out.println("Enter the first word1: ");
                      String word1 = scanner.nextLine();
                      System.out.println("Enter the second word2: ");
                      String word2 = scanner.nextLine();
                      
                      calculateDistance(distanceType, word1, word2);
                     break;
                	 
                case 6:
                	// Exit the application
                    running = false;
                    System.out.println("Exiting application.");
                    break;
                    
                default:
                	// Handle invalid menu options.
                    System.out.println("Invalid choice. Please try again.");
            
            		}
        		}  
        
    

        scanner.close();
        
        }
    	
        // Helper class to store information about a word match. 
	    private static class Match {
	        String word;  // The word associated with this match.
	        int position; // The position of the word in the embeddings array.
	        double value; // The value representing the similarity or distance metric for this match.
	     
	     /*
	      * Constructor to initialize a Match object with the given word, position and value.
	      * @parameter "word" - The word that is being matched.
	      * @parameter "position" - The position of the word in the embeddings array.
	      * @parameter "value" - The value representing the similarity or distance metric for this match.
	      */
	    Match(String word, int position, double value) {
	        this.word = word;
	        this.position = position;
	        this.value = value;
	            
	        }
	    }
	    
	    // Method to configure similarity comparison options.
	    private void configureOptions(Scanner scanner) {
	        System.out.println("Configure Options");
	        System.out.println("Choose similarity measure for comparison against 'prince': ");
	        System.out.println("1. Dot Product");
	        System.out.println("2. Cosine Similarity");
	        System.out.println("3. Euclidean Distance");
	        
	        /*
             *  Source from https://stackoverflow.com/questions/5794103/text-similarity-algorithms
             *  
             *  Modified to do the following code (comparing prince vector to other vectors). 
             */

	        try {
	            if (scanner.hasNextInt()) {
	                int option = scanner.nextInt();
	                scanner.nextLine(); // Consume newline after the number
	                
	                // Find the index of the word "prince" in the embeddings array.
	                int index = findWordIndex("prince");
	                if (index == -1) {
	                    System.out.println("'prince' not found in the embeddings.");
	                    return;
	                }
	                
	                
	                /*
	                 *  Source from https://stackoverflow.com/questions/5794103/text-similarity-algorithms
	                 *  
	                 *  Modified to do the following code (comparing prince vector to other vectors). 
	                 */
	                double[] princeVector = embeddings[index];
	                Match[] bestMatches = new Match[10];
	                
	                // Iterates through all words in the embeddings array
	                for (int i = 0; i < words.length; i++) {
	                    if (i != index) { // Skip comparison with the word "prince" itself
	                        double[] comparingVector = embeddings[i]; // Get the embedding vector for the current word.
	                        double value = 0.0;
	                        
	                        // Calculate the similarity or distance based on the selected option.
	                        switch (option) {
	                            case 1:
	                                value = dotProduct(princeVector, comparingVector);
	                                break;
	                            case 2:
	                                value = cosineSimilarity(princeVector, comparingVector);
	                                break;
	                            case 3:
	                                value = euclideanDistance(princeVector, comparingVector);
	                                break;
	                            default:
	                                System.out.println("Invalid option selected.");
	                                return;
	                        }
	                        
	                        // Insert the current word into the bestMatches array if it qualifies as one of the top 10 matches.
	                        for (int j = 0; j < bestMatches.length; j++) {
	                            if (bestMatches[j] == null || value > bestMatches[j].value) {
	                            	
	                                for (int k = bestMatches.length - 1; k > j; k--) {
	                                    bestMatches[k] = bestMatches[k - 1];
	                                }
	                                
	                                bestMatches[j] = new Match(words[i], i, value);
	                                break;
	                            }
	                        }
	                    }
	                }
	                
	                // Display the top 10 matches to the user.
	                System.out.println("Top 10 matches:");
	                for (Match match : bestMatches) {
	                    if (match != null) {
	                        System.out.println("Best match is word " + match.word + " at position " + match.position + " with value " + match.value);
	                    }
	                }
	            } else {
	                System.out.println("Invalid input. Please enter a number.");
	                scanner.nextLine(); // Clear the buffer
	            }
	        } catch (Exception e) {
	            System.err.println("An error occurred: " + e.getMessage());
	            scanner.nextLine(); // Clear the buffer in case of errors
	        }
	    }


	    /* 
	     * Method to calculate the similarity or dot product or distance between the embeddings of two words
	     * based on the specified distance type. 
	     * 
	     * @parameter "distanceType" - The type of distance or similarity measure to use for 
	     * 1. Cosine Similarity 2. Dot Product 3. Euclidean Distance.
	     * 
	     * @parameter "word1" - The first word for which the distance is to be calculated.
	     * @parameter "word2" - The second word for which the distance is to be calculated.
	     */
	    private void calculateDistance(int distanceType, String word1, String word2) {
	    	
	    	// Find the indices of the two words in the embeddings array. 
	        int index1 = findWordIndex(word1);
	        int index2 = findWordIndex(word2);
	        
	        // Check if both words are found in the embeddings.
	        if (index1 != -1 && index2 != -1) {
	        	
	        	// Retrieve the embeddings for the two words.
	            double[] vector1 = embeddings[index1];
	            double[] vector2 = embeddings[index2];
	
	            double result = 0.0; // Variable to store the result of the distance calculation.
	            String distanceTypeName = "";   // Variable to store the name of the distance type.
	            
	            // Calculate the similarity or distance type based on the specified distance type.
	            switch (distanceType) {
	            
	                case 1:
	                	// Calculate Cosine Similarity
	                    result = cosineSimilarity(vector1, vector2);
	                    distanceTypeName = "Cosine Similarity";
	                    break;
	                    
	                case 2:
	                	// Calculate Dot Product
	                    result = dotProduct(vector1, vector2);
	                    distanceTypeName = "Dot Product";
	                    break;
	                    
	                case 3:
	                	// Calculate Euclidean Distance
	                    result = euclideanDistance(vector1, vector2);
	                    distanceTypeName = "Euclidean Distance";
	                    break;
	                    
	                default: 
	                	// Handle invalid distance type.
	                	System.out.println("Invalid distance type specified.");
	            }
	            
	            // Display the calculated distance or similarity.
	            System.out.println(distanceTypeName + " between " + word1 + " and " + word2 + ": " + result);
	            
	        } else {
	        	// Handle the case where one or both words are not found in the embeddings.
	            System.out.println("One of the words is not in the embeddings list.");
	        }
	    }
	
	     
	    /*
	     * Method to process a mathematical expression involving word embeddings.
	     * The expression can include addition (+) and subtraction (-)
	     * operations applied to the vectors for words.
	     * @parameter "expression" -  The mathematical expression involving words and operators.
	     */
	    private void processExpression(String expression) {
	        String[] keywords = expression.split("(?=[+-])|(?<=[+-])"); // Split by + or -
	        double[] output = new double[FEATURE_COUNT]; // Initialize the result vector 
	        boolean Word1 = true;  // Flag to handle the first word.
	        
	        // Iterate through each keyword in the expression
	        for (int i = 0; i < keywords.length; i++) {
	            String keyword = keywords[i].trim();
	            
	            // Skip processing for operators and just print them.
	            if (keyword.equals("+") || keyword.equals("-")) {
	                System.out.println("Operation: " + keyword);
	                continue;
	            }
	            
	            // Find the index of the word in the words array.
	            int index = findWordIndex(keyword);
	            
	            // If the word is found found, process its embedding.
	            if (index != -1) {
	                System.out.println("Position of word '" + keyword + "' in words[]: " + index);
	                System.out.println(Arrays.toString(embeddings[index]));
	                
	                // Handle the first word in the expression.
	                if (Word1) {
	                	// Initialize the output vector with the first word's embedding.
	                	output = Arrays.copyOf(embeddings[index], FEATURE_COUNT);
	                    Word1 = false; // Mark that the first word has been processed.
	                    
	                } else {
	                	
	                	// Perform vector addition or subtraction based on the previous vector.
	                    if (keywords[i - 1].equals("+")) {
	                    	output = vectorAdd(output, embeddings[index]);
	                    } else if (keywords[i - 1].equals("-")) {
	                    	output = vectorSubtract(output, embeddings[index]);
	                    }
	                }
	                
	                // Print the result after the current operation.
	                System.out.println("Result after this operation: " + Arrays.toString(output));
	                
	            } else {
	            	
	            	// Handle the case where the word is not found in the embeddings.
	                System.out.println("Word '" + keyword + "' not found in the embeddings.");
	            }
	        }
	    }
	    
	    /*
	     * Adds two vectors element-wise.
	     * @parameter "vector1" - The first input vector (array of doubles).
	     * @parameter "vector2" - The second input vector (array of doubles).
	     * @return A new vector that is the element-wise sum of the two input vectors.
	     * 
	     * Source from  https://www.youtube.com/watch?v=P0f7CLUcbD8 AND https://stackoverflow.com/questions/65612062/how-do-i-subtract-and-add-vectors-with-gensim-keyedvectors
	     * 
	     * Modified to do the following code.
	     */
	    private double[] vectorAdd(double[] vector1, double[] vector2) {
	        double[] output = new double[FEATURE_COUNT]; // Create an array to store the result.
	        for (int i = 0; i < FEATURE_COUNT; i++) {
	            output[i] = vector1[i] + vector2[i];   // Add corresponding elements from both vectors.
	        }
	        return output;  // Return the resulting(output) vector.
	    }
	    
	    /*
	     * Subtracts the second vector from the first vector element-wise.
	     * @parameter "vector1" - The first input vector (array of doubles).
	     * @parameter "vector2" - The second input vector (array of doubles).
	     * @return A new vector that is the element-wise difference between the two input vectors.
	     * 
	     * Source from  https://www.youtube.com/watch?v=P0f7CLUcbD8 AND https://stackoverflow.com/questions/65612062/how-do-i-subtract-and-add-vectors-with-gensim-keyedvectors
	     * 
	     * Modified to do the following code.
	     */
	    private double[] vectorSubtract(double[] vector1, double[] vector2) {
	        double[] output = new double[FEATURE_COUNT];  // Create an array to store the result.
	        for (int i = 0; i < FEATURE_COUNT; i++) {
	            output[i] = vector1[i] - vector2[i];  // Subtract corresponding elements of the second vector from the first vector.
	        }
	        return output;  // Return the resulting(output) vector.
	    }
	
	
	
	 /*
	  *  Method to find the index of a word in the words array.
	  *  @parameter "word" - The word to find in the array.
	  *  @return The index of the word in the words array, or -1 if the word is not found.
	  *  
	  *  Source from https://stackoverflow.com/questions/53035839/how-to-calculate-similarity-for-pre-trained-word-embeddings
	  *  
	  *  Modified to do the following code.
	  */
	    public int findWordIndex(String word) {
	        for (int i = 0; i < words.length; i++) {
	            if (words[i].equals(word)) {
	                return i;
	            }
	        }
	        return -1; // Return -1 if the word is not found
	    }
	
		/*
		 *  Retrieves the embedding vector for a given word.
		 *  @parameter "word" - The word for which the embedding is requested.
		 */
		public double[] getEmbeddingsForWord(String word) {
			for (int i = 0; i < words.length; i++) {
				if(words[i].equals(word)) { // Check if the current word matches the the input word.
				return embeddings[i];  // Return the corresponding embedding vector if the word is found
			}
		}
			return null; // Return null if the word is not found in the array.
	}
		
		
		
		 /*
	     * Calculates the cosine similarity between two vectors.
	     * @parameter "vector1" - The first vector.
	     * @parameter "vector2" - The second vector.
	     * @return The cosine similarity between two vectors.
	     * 
	     * Source from https://stackoverflow.com/questions/1746501/can-someone-give-an-example-of-cosine-similarity-in-a-very-simple-graphical-wa
	     * 
	     * Modified to do cosineSimilarity
	     */
		public double cosineSimilarity(double[] vector1, double[] vector2) {
	        double dotProduct = 0.0;
	        double normA = 0.0;
	        double normB = 0.0;
	        for (int i = 0; i < vector1.length; i++) {
	            dotProduct += vector1[i] * vector2[i];
	            normA += Math.pow(vector1[i], 2);
	            normB += Math.pow(vector2[i], 2);
	        }
	        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
	    }
	    
		 /*
	     * Calculates the dot product between two vectors.
	     * @parameter "vector1" - The first vector.
	     * @parameter "vector2" - The second vector.
	     * @return The dot product between two vectors.
	     * 
	     * Source from https://stackoverflow.com/questions/1746501/can-someone-give-an-example-of-cosine-similarity-in-a-very-simple-graphical-wa
	     * 
	     * Modified to do dot product
	     */
	    public  double dotProduct(double[] vector1, double[] vector2) {
	    	double dotProduct = 0.0;
	    	for (int i = 0; i < vector1.length; i++) {
	    		dotProduct += vector1[i] * vector2[i];
	    	}
	    	return dotProduct;
	    }
	    
	    /*
	     * Calculates the euclidean distance between two vectors.
	     * @parameter "vector1" - The first vector.
	     * @parameter "vector2" - The second vector.
	     * @return The euclidean distance between two vectors.
	     * 
	     * Source from https://stackoverflow.com/questions/1746501/can-someone-give-an-example-of-cosine-similarity-in-a-very-simple-graphical-wa
	     * 
	     * Modified to do euclidean distance
	     */
		public  static double euclideanDistance(double[] vector1, double[] vector2) {
			double sum = 0.0;
			for (int i = 0; i < vector1.length; i++) {
				sum += Math.pow(vector1[i] - vector2[i], 2);
				
			}
			return Math.sqrt(sum);
		}
		
	
	
	
		private String outputPath(String string) {
		
			return string;
		}
	
		
		
		
		/*
		 *  Method to load word embedding from a file.
		 *  
		 *  Source from https://www.youtube.com/watch?v=lHFlAYaNfdo  AND https://www.youtube.com/watch?v=M8xFzcWiORE
		 *  AND CLASS NOTES (Source code).
		 *  
		 *  Modified to do the following code.
		 */
	    public void loadEmbeddings(String filePath) throws IOException {
	    	
	        // Implementation of loading embedding from the file
	        System.out.println("Loading embeddings from: " + filePath);
	        FileHandler fileHandler = new FileHandler();
	        String[][] data = fileHandler.readEmbeddings(filePath);
	        int rowCount = data.length;
	
	        words = new String[rowCount];
	        embeddings = new double[rowCount][FEATURE_COUNT];
	
	        for (int i = 0; i < rowCount; i++) {
	        	
	        	//load the words into array
	            words[i] = data[i][0].replace(",", "");
	            for (int j = 0; j < FEATURE_COUNT; j++) {
	            	
	                //Replace comma at the end of each value and store it in embedding
	            	embeddings[i][j] = Double.parseDouble((data[i][j + 1]).replace(",", ""));
	            }
	        }
	        System.out.println("Embeddings loaded successfully.");
	        
	 
	    }
	
	    /*
	     * Method to find similar words.
	     * 
	     * Source from https://stackoverflow.com/questions/53035839/how-to-calculate-similarity-for-pre-trained-word-embeddings
	     * 
	     * Modified to do the following code.
	     */
	    
	    
	    public void findSimilarWords(String userInput, int n, String outputPath) throws IOException {
	        SimilarityCalculator calculator = new SimilarityCalculator(words, embeddings);       
	        calculator.calculateSimilarity(userInput, n, outputPath);
	        System.out.println("Results written to " + outputPath);
	    }
	
		
	}
