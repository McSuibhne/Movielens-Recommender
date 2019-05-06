package ucd.ai.cf;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Set;

/**
 * DatasetWriter writes Profiles back out to a file that can be read in later
 */
public class DatasetWriter {
	private String dataFilename;

	/** Constructs a DatasetWriter object with the given profile and movie files to write to
	 * @param itemFile the path to the item file, e.g. "MovieLens/u.item"
	 * @param dataFile the path to the profile file, e.g. "MovieLens/u-filtered.data"*/
	public DatasetWriter(final String itemFile, final String dataFile) {
		this.dataFilename = dataFile;
	}

	/**This writes the given set of profiles to file.
	 * @param profiles is the set containing the profiles to be written.*/
	public void writeData(final Set<Profile> profiles) throws Exception{
		BufferedWriter writer = new BufferedWriter(new FileWriter(dataFilename));
		for (Profile profile: profiles) {
			for (Rating rating: profile.getRatings()) {
				writer.write(rating.toString());
				writer.newLine();
			}
		}
		writer.close();
	}
}
