package ucd.ai.cf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * DatasetReader is used to read in the profiles from the Movie Lens dataset. It can load the complete profiles or it can split
 * the ratings into source and target sets for evaluation.
 */

public class DatasetReader {
	private String movieFile;
	private String dataFile;
	private HashMap<Integer, Movie> movieMap = null;
	private HashMap<Integer, Profile> dataMap = null;

	/** Constucts a DatasetReader from the MovieLens profiles
	 * 
	 * @param movieFileAddr	the path of the file containing movies descriptions
	 * @param dataFileAddr  the path of the file containing the user profiles
	 */
	public DatasetReader(final String movieFileAddr, final String dataFileAddr){
		this.movieFile = movieFileAddr;
		this.dataFile = dataFileAddr;
	}

	/** This configures the DatasetReader to load all of the ratings in the profiles.
	 * 
	 * @throws Exception Throws an error if it can't load files
	 */
	public void loadAllProfiles() throws Exception{
		loadMovies(movieFile);
		loadProfiles(dataFile);
	}

	/** This configures the DatasetReader to load the profiles but split the profile ratings
	 * into target and source
	 * @param targetPercentage the percentage of ratings to give to the target set. e.g 0.2 means remove 20% of the ratings and put them in the target set
	 */
	public void loadEvaluationProfiles(final double targetPercentage) throws Exception{
		loadAllProfiles();
		for (Profile profile: dataMap.values()) {
			profile.split(targetPercentage);
		}
	}

	/**Returns all the profiles loaded. loadAllProfiles() or loadEvaluationProfiles() must be called before this method is.
	 * @return a Set containing Profile objects
	 */
	public Set<Profile> getProfiles(){
		return new HashSet<Profile>(dataMap.values());
	}

	private void loadProfiles(final String dataFile) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(new File(dataFile)));
		String line;
		dataMap = new HashMap<Integer, Profile>();
		while ((line = in.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(line, "\t");
			Integer userId = Integer.valueOf(st.nextToken());
			Integer movieId = Integer.valueOf(st.nextToken());
			double score = Double.valueOf(st.nextToken()).doubleValue();
			Rating rating = new Rating(score, movieMap.get(movieId));
			Profile p;
			if(dataMap.containsKey(userId))
				p = dataMap.get(userId);
			else
				p = new Profile(userId);
			p.addRating(rating);
			rating.setProfile(p);
			dataMap.put(userId, p);
		}
		in.close();
	}

	private void loadMovies(final String movieFile) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(new File(movieFile)));
		String line;
		movieMap = new HashMap<Integer, Movie>();
		while ((line = in.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(line, "|");
			Integer id = Integer.valueOf(st.nextToken());
			String name = st.nextToken();
			Movie movie = new Movie(name, id);
			movieMap.put(id, movie);
		}
		in.close();
	}
}
