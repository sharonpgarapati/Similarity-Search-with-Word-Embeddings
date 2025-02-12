package ie.atu.sw;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Comparator;

public class SimilarityCalculator {

	    private String[] words;			// Array to store words
	    private double[][] embeddings;  // 2D array to store embeddings for each word
	    
	    // Constructor to initialize the words and embeddings arrays
	    public SimilarityCalculator(String[] words, double[][] embeddings) {
	        this.words = words;
	        this.embeddings = embeddings;
	    }
	    
	    /*
	     * Calculates similarity between the input text and all words in the embeddings, 
	     * and writes the top 'n' similar words to the specified output file. 
	     * @parameter "userInput" - The input text or word.
	     * @parameter "n" - The number of top similar words to find.
	     * @parameter "outPutPath" -  The path of the output file where results will be written.
	     * @throws "IOException" - If an Input/Output error occurs while writing the output file.  
	     * 
	     * Source from https://stackoverflow.com/questions/955110/similarity-string-comparison-in-java
	     * 
	     * Modified to do the following code.
	     */
	    public void calculateSimilarity(String userInput, int n, String outputPath) throws IOException {
	    	
	    	// Get the vector representation for the user Input.
	        double[] inputVector = getVectorForInput(userInput);
	        
	        // Priority Queue to store top 'n' similar words with their similarity score.
	        PriorityQueue<String> topWords = new PriorityQueue<>(n, Comparator.comparingDouble(this::getSimilarity));
	        
	        // Iterate through all words to calculate similarity.
	        for (int i = 0; i < words.length; i++) {
	        	
	        	// Calculate cosine similarity between input vector and current word's vector.
	            double similarity = cosineSimilarity(inputVector, embeddings[i]);
	            if (topWords.size() < n) {
	            	
	            	// If less than 'n' elements, add the new word.
	                topWords.offer(words[i] + " " + similarity);
	            } else if (similarity > getSimilarity(topWords.peek())) {
	            	
	            	// If current similarity is greater than the smallest in the queue, replace it.
	                topWords.poll();
	                topWords.offer(words[i] + " " + similarity);
	            }
	        }
	        
	        // Write the top similar words to the output file.
	        writeOutput(topWords, outputPath);
	    }
	    
	    /*
	     * Extracts similarity score from a string entry in the PriorityQueue.
	     * @parameter "entry" - The string entry from the PriorityQueue.
	     * @return The similarity score as double. g
	     */
	    private double getSimilarity(String entry) {
	        return Double.parseDouble(entry.split(" ")[1]);
	    }
	    
	    /*
	     * Writes the top similar words and their similarity scores to the specified output file.
	     * @parameter "topWords" - The PriorityQueue containing top similar words.
	     * @parameter "outputPath" - The path of the output file where results will be written.
	     * @throws " IOException" -  If an Input/Output error occurs while writing the output file.
	     * 
	     *  Source from https://www.youtube.com/watch?v=lHFlAYaNfdo  AND https://www.youtube.com/watch?v=M8xFzcWiORE
		 *  AND CLASS NOTES (Source code).
	     * 
	     *  Modified to do the following code.
	     */
	    private void writeOutput(PriorityQueue<String> topWords, String outputPath) throws IOException {
	        BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath));
	        
	        // Write each word and its similarity score to the output file.
	        while (!topWords.isEmpty()) {
	            writer.write(topWords.poll() + "\n");
	        }
	        writer.close();
	    }
	
	    /*
	     * Generates a vector representation of the input text by averaging the vectors of individual words.
	     * @parameter "input" - The input text containing words.
	     * @return "inputVector" - The inputVector represents the input text.
	     */
	    public double[] getVectorForInput(String input) {
	        String[] inputWords = input.split(" ");
	      
	        double[] inputVector = new double[embeddings[0].length];
	        for (String word : inputWords) {
	            for (int i = 0; i < this.words.length; i++) {
	                if (this.words[i].equalsIgnoreCase(word)) {
	                    for (int j = 0; j < inputVector.length; j++) {
	                        inputVector[j] += embeddings[i][j];
	                    }
	                    break;
	                }
	            }
	        }
	
	        for (int i = 0; i < inputVector.length; i++) {
	            inputVector[i] /= inputWords.length;
	        }
	
	        return inputVector;
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
	    
	    public static double cosineSimilarity(double[] vector1, double[] vector2) {
	        double dotProduct = 0.0;
	        double normA = 0.0;
	        double normB = 0.0;
	        for (int i = 0; i < vector1.length; i++) {
	            dotProduct += vector1[i] * vector2[i];
	            normA += Math.pow(vector1[i], 2);
	            normB += Math.pow(vector2[i], 2);
	        }
	        // Return the value of cosine similarity.
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
	    
	    public  static double dotProduct(double[] vector1, double[] vector2) {
	    	double dotProduct = 0.0;
	    	for (int i = 0; i < vector1.length; i++) {
	    		dotProduct += vector1[i] * vector2[i];
	    	}
	    	return dotProduct; // Return the value of dot product.
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
			return Math.sqrt(sum); // Return the square root of the sum (euclidean distance).
		}
	}
