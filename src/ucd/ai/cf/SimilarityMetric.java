package ucd.ai.cf;

import java.util.Set;

public interface SimilarityMetric {

	public double computeSimilarity(Profile profileA, Profile profileB);

	public double predictRating(Profile profile, Movie movie, double threshold);

	public Set<Profile> getProfileSet();

}
