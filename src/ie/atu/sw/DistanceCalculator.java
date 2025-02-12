package ie.atu.sw;

public class DistanceCalculator {
	
	// Inner class that handles the calculation of different distance metrics.
	public class DisCalculator {

        /*
         * Calculates the distance between two vectors based on the specified type.
         * @parameter "type" - The type of distance metric to use ("cosine similarity", "dot product", "euclidean distance").
         * @parameter "vector1" - The first vector.
         * @parameter "vector2" - The second vector.
         * @return The calculated similarity or dot product or distance value.
         */
		
        public double calculateDistance(String type, double[] vector1, double[] vector2) {
        	
        	// Determine which distance metric to use based on the type of parameter.
			switch (type.toLowerCase()) {
			case "cosine":
				return cosineSimilarity(vector1, vector2);
			case "dotProduct":
				return dotProduct(vector1, vector2);
			case "euclidean" :
				return euclideanDistance(vector1, vector2);
			default:
				
				// Throw an exception if the provided type is not supported.
				throw new IllegalArgumentException("Unsupported distance type: " + type);
			}
			
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
        public  double dotProduct(double[] vector1, double[] vector2) {
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
    		return Math.sqrt(sum); // Return the square root of the sum (euclidean). 
    	}
		}
	
}

