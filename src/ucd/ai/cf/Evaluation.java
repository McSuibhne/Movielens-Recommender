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

	/**Calculates the percentage of targets for which the metric was able to make a prediction.
	 * @param threshold the threshold to pass into the similarity metric.
	 * @return the percentage of targets for which a recommendation was made (e.g. 0.5 = 50%)
	 */
	public double getPercentageRecommended(final double threshold){
		double failed_recommendations = 0, total_movies = 0;

		for(Profile current_profile: metric.getProfileSet()){
			for(Movie current_movie: current_profile.getTargetMovieList()){
				if(metric.predictRating(current_profile, current_movie, threshold) == -1){
					failed_recommendations++;
				}
			}
			total_movies += current_profile.getTargetMovieList().size();
		}

		return (total_movies - failed_recommendations)/total_movies;
	}

	/**Calculates the standard deviation of the errors.
	 * @param threshold the threshold to pass into the similarity metric.
	 * @return the standard deviation of errors.
	 */
	public double getStdDeviationError(final double threshold) {
		double total_dist = 0, total_movies = 0, mean_absolute_error = getMeanAbsoluteError(threshold), errors = 0;
		for(Profile current_profile: metric.getProfileSet()){
			for(Movie current_movie: current_profile.getTargetMovieList()){
				if(metric.predictRating(current_profile, current_movie, threshold) != -1){
					errors = Math.abs(metric.predictRating(current_profile, current_movie, threshold) - current_profile.getTargetRating(current_movie));
					total_dist += Math.pow((errors - mean_absolute_error), 2);
					total_movies++;
				}
			}
		}
		return Math.sqrt(total_dist/total_movies);
	}

	/**Calculates the mean absolute error for the the given similarity metric.
	 * In this case, for actual ratings of 1, 2, 3, 4, and 5, this method prints:
	 * - the actual rating
	 * - the number of occurrences of the actual rating in the target set
	 * - the mean MAE calculated over each actual rating
	 * @param threshold the threshold to pass into the similarity metric.
	 */
	public void getMeanAbsoluteErrorDist(final double threshold) {
		double[] ratingscounts = new double[5];
		double[] predictionerrors = new double[5];


		for(Profile current_profile: metric.getProfileSet()){
			for(Movie current_movie: current_profile.getTargetMovieList()){
				ratingscounts[(int) ((current_profile.getTargetRating(current_movie))-1)]++;
				if(metric.predictRating(current_profile, current_movie, threshold) != -1) {
					predictionerrors[(int) ((current_profile.getTargetRating(current_movie))-1)] += Math.abs(metric.predictRating(current_profile, current_movie, threshold) - current_profile.getTargetRating(current_movie));
				}
			}
		}

		System.out.println("Number of ratings"+ "\tMAE");
		for(int i=0; i<5; i++){
			System.out.println(i+1 + ": " + ratingscounts[i] + " \t\t " + predictionerrors[i]/ratingscounts[i]);
		}
	}


	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(final double threshold) {
			this.threshold = threshold;
	}
}
