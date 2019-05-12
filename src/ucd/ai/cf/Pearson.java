package ucd.ai.cf;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is used to compute the pearson profile similarity metric and also to predict ratings
 */
public class Pearson implements SimilarityMetric{

	/**Constructor for Pearson
	 * @param profileSet the set of profiles on which Pearson will operate
	 * Examples of use:
	 * Set profiles = .... load from files
	 * Pearson pearson = new Pearson(profiles);
	 */
	public Pearson(final Set<Profile> profileSet){
		setup(profileSet);
	}

	/**Computes the pearson correlation coefficient (similarity) between 2 profiles.
	 * @param a The first profile to compare
	 * @param b The second profile
	 * @return the pearson profile similarity between the 2 profiles
	 */
	public double computeSimilarity(final Profile a, final Profile b) {
		Set<Movie> commonMovies = a.getCommonMovies(b);
		double aAverageRating = a.getMeanRating();
		double bAverageRating = b.getMeanRating();
		double top = 0;
		double bottomA = 0;
		double bottomB = 0;
		for (Movie movie: commonMovies) {
			double ad = a.getRatingFor(movie) - aAverageRating;
			double bd = b.getRatingFor(movie) - bAverageRating;
			top += (ad * bd);
			bottomA += (ad * ad);
			bottomB += (bd * bd);
		}
		double bottom = Math.sqrt(bottomA * bottomB);
		if(bottom > 0) {
			if(commonMovies.size() < 50) {
				return (commonMovies.size() * 1.0 / 50) * (top / bottom);
			} else {
				return top / bottom;
			}
		} else {
			return 0;
		}
	}


	/**Predicts the rating for a movie for the given profile using the Pearson similarity metric
	 * @param profile the profile for which the rating will be predicted
	 * @param m the movie for which the rating will be made
	 * @param minThreshold the maximum dissimilarity threshold
	 * @return the predicted rating that the owner of that profile would have made for that movie
	 */
	public double predictRating(final Profile profile, final Movie m,
			final double minThreshold) {
		Set<Profile> neighbours = computeNeighbours(profile, minThreshold);
		double top = 0;
		double bottom = 0;
		for (Profile p: neighbours) {
			if(p.hasRated(m)) {
				top += getPearson(profile, p) *
						(p.getRatingFor(m) - p.getMeanRating());
				bottom += Math.abs(getPearson(profile, p));
			}
		}
		if(bottom > 0) {
			double prediction = profile.getMeanRating() + top / bottom;
			if(prediction < MIN_RATING) {
				prediction = MIN_RATING;
			}
			if(prediction > MAX_RATING) {
				prediction = MAX_RATING;
			}
			return prediction;
		} else {
			return -1;
		}
	}


	/**
	 * Computes the set of neighbours that will be used in the prediction of a movie rating for a given user profile.
	 * @param profile the profile for which the neighbourhood will be found
	 * @param simThreshold the maximum dissimilarity threshold for the neighbours
	 * @return the set of neighbours that are most similar to the given profile
	 */
	private Set<Profile> computeNeighbours(final Profile profile,
			final double simThreshold) {
		Set<Profile> n = new HashSet<Profile>();
		for (Profile candidate: profileSet) {
			if(profile.getUserId() != candidate.getUserId()) {
				if(getPearson(profile, candidate) > simThreshold) {
					n.add(candidate);
				}
			}
		}
		return n;
	}


	private static double MIN_RATING = 1;
	private static double MAX_RATING = 5;
	private double[][] simMatrix = null;//hold all the computed Pearson values
	private Set<Profile> profileSet;

	private void setup(final Set<Profile> profiles){
		simMatrix = new double[profiles.size()][profiles.size()];
		this.profileSet = profiles;
		computeAllSimilarity(profiles);
	}

	private void computeAllSimilarity(final Set<Profile> set){
		for (Profile a: set){
			for (Profile b: set) {
				setSim(a, b, computeSimilarity(a, b));
			}
		}
	}

	/**Stores the Pearson correlation coefficent (similary value) in memory between 2 profiles
	 * @param a First profile
	 * @param b Second profile
	 * @param value the similarity between the profiles
	 */
	private void setSim(final Profile a, final Profile b, final double value) {
		simMatrix[a.internalID()][b.internalID()] = value;
		simMatrix[b.internalID()][a.internalID()] = value;
	}

	/**Retrieves the previously computed Pearson value between 2 profiles from memory
	 * @param profile - First profile
	 * @param candidate - Second profile
	 * @return the MSD value for the 2 profiles
	 */
	private double getPearson(final Profile profile, final Profile candidate) {
		return simMatrix[profile.internalID()][candidate.internalID()];
	}

	/**@return Returns the set of profiles that the similarity metric is working on.
	 */
	public Set<Profile> getProfileSet() {
		return profileSet;
	}

	/**Computes the average rating given by a user for a set of given movies
	 * @param profile the profile in question
	 * @param commonMovies the set of movies for which ratings were given
	 * @return the average rating given by user p for the set of movies
	 */
	protected double calcAverageRating(final Profile profile, final Set<Movie> commonMovies) {
		double total = 0;
		for (Movie movie: commonMovies) {
			total = total + profile.getRatingFor(movie);
		}
		return total / commonMovies.size();
	}
}
