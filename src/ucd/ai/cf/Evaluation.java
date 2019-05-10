package ucd.ai.cf;

import java.util.Iterator;


/**
 * The Evaluation class runs a series of tests on a Similarity Metric,
 * e.g MeanSquaredDifference or Pearson in order to asses their recommendation performance.
 */
public class Evaluation {

	//the metric to be evaluated
	private SimilarityMetric metric;
	private double threshold = Double.MIN_VALUE;

	/**Constructs an instance of Evaluation
	 * @param metric the similarity metric to be evaluated, e.g. an instance of Pearson or MeanSquaredDifference
	 * Example of use;
	 *  Pearson pearson = new Peason(profiles);
	 *  Evaluation eval = new Evaluation(pearson);
	 *  System.out.println(eval.getMeanAbsoluteError(2.0)); - prints out the mean absolute error for pearson at that threshold
	 */
	public Evaluation(SimilarityMetric metric) {
		this.metric = metric;
	}

	/**Calculates the mean absolute error for the the given similarity metric.
	 * @param threshold the threshold to pass into the similarity metric.
	 * @return the mean absolute error
	 */
	public double getMeanAbsoluteError(final double threshold){
		double total_error = 0, total_movies = 0;
		for(Profile current_profile: metric.getProfileSet()){
			for(Movie current_movie: current_profile.getTargetMovieList()){
				if(metric.predictRating(current_profile, current_movie, threshold) != -1){
					total_error += Math.abs(metric.predictRating(current_profile, current_movie, threshold) - current_profile.getTargetRating(current_movie));
					total_movies++;
				}
			}
		}
		return total_error/total_movies;
	}

	
}
