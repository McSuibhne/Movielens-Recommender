package ucd.ai.cf;

import java.util.HashSet;
import java.util.Set;

/**This class is used to compute the mean squared difference profile similarity metric and also to predict ratings*/
public class MeanSquaredDifference implements SimilarityMetric{

	/**
	 * Constructor for MSD
	 * @param profileSet the set of profiles on which the MSD will operate
	 * Examples of use:
	 * Set profiles = .... load from files
	 * MeanSquaredDifference msd = new MeanSquaredDifference(profiles);
	 */
	public MeanSquaredDifference(final Set<Profile> profileSet){
		setup(profileSet);
	}

	/**Computes the MSD similarity between 2 profiles.
	 * @param a The first profile to compare
	 * @param b The second profile
	 * @return the MSD similarity between the 2 profiles
	 */
	public double computeSimilarity(final Profile a, final Profile b) {
		Set<Movie> commonMovies = a.getCommonMovies(b);
		double sqdiff = 0;
		if(commonMovies.size() > 0) {
			for (Movie movie: commonMovies) {
				double A = a.getRatingFor(movie);
				double B = b.getRatingFor(movie);
				sqdiff += Math.pow(A - B, 2);
			}
			sqdiff = sqdiff/commonMovies.size();
			double sim = 1 - sqdiff / Math.pow(MAX_RATING - MIN_RATING, 2);
			if(commonMovies.size() < 50) {
				sim = (commonMovies.size() * 1.0 / 50) * sim;
			}
			return sim;
		} else {
			return 0;
		}
	}


	/**Predicts the rating for a movie for the given profile using the MSD metric
	 * @param profile the profile for which the rating will be prdicted
	 * @param movie the movie for which the rating will be made
	 * @param simThreshold the maximum dissimilarity threshold
	 * @return the predicted rating that the owner of that profile would have made for that movie
	 */
	public double predictRating(final Profile profile, final Movie movie,
			final double simThreshold) {
		Set<Profile> neighbours = computeNeighbours(profile, simThreshold);
		double top = 0;
		double bottom = 0;
		for (Profile p: neighbours) {
			if(p.hasRated(movie)) {
				top += getMSD(profile, p) * p.getRatingFor(movie);
				bottom += getMSD(profile, p);
			}
		}
		if(bottom > 0) {
			double prediction = top / bottom;
			if(prediction < MIN_RATING) {
				prediction = MIN_RATING;
			}
			if(prediction > MAX_RATING) {
				prediction = MAX_RATING;
			}
			return prediction;
		}
		else {
			return -1;
		}
	}


	/**Computes the set of neighbours that will be used in the prediction of a movie rating for a given user.
	 * @param profile	The profile for which the neighbourhood will be found
	 * @param simThreshold the maximum dissimilarity threshold for the neighbours
	 * @return the set of neighbours that are most similar to the given profile
	 */
	private Set<Profile> computeNeighbours(final Profile profile,
			final double simThreshold) {
		Set<Profile> n = new HashSet<Profile>();
		for (Profile candidate: profileSet) {
			if(profile.getUserId() != candidate.getUserId()) {
				if(getMSD(profile, candidate) > simThreshold) {
					n.add(candidate);
				}
			}
		}
		return n;
	}

	private static double MIN_RATING = 1;
	private static double MAX_RATING = 5;
	private double[][] matrix = null;//holds all the computed MSDs
	private Set<Profile> profileSet;

	private void setup(final Set<Profile> profiles){
		matrix = new double[profiles.size()][profiles.size()];
		computeAllMSD(profiles);
		this.profileSet = profiles;
	}

	private void computeAllMSD(final Set<Profile> set){
		for (Profile a: set){
			for (Profile b: set){
				setMSD(a, b, computeSimilarity(a, b));
			}
		}
	}

	/**
	 * Stores the Means Squared Difference (MSD) similarity value in memory between 2 profiles
	 * @param a First profile
	 * @param b Second profile
	 * @param value the similarity between to 2.
	 */
	private void setMSD(final Profile a, final Profile b, final double value) {
		matrix[a.internalID()][b.internalID()] = value;
		matrix[b.internalID()][a.internalID()] = value;
	}

	/**Retrieves the previously computed MSD value between 2 profiles from memory
	 * @param a First profile
	 * @param b Second profile
	 * @return the MSD value for the 2 profiles
	 */
	private double getMSD(final Profile a, final Profile b) {
		return matrix[a.internalID()][b.internalID()];
	}

	/**@return Returns the set of profiles that the similarity metric is working on.*/
	public Set<Profile> getProfileSet() {
		return profileSet;
	}

}
