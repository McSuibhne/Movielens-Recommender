package ucd.ai.cf;

public class Rating {

	private Profile profile;//the profile who gave the rating
	private double rating;//the rating
	private Movie movie;//the movie that was rated

	/**constructor - creates a new rating object and fills in the fields
	 * @param profile
	 * @param rating
	 * @param movie
	 */
	public Rating(final Profile profile, final double rating, final Movie movie){
		this.profile = profile;
		this.rating = rating;
		this.movie = movie;
	}


	/**
	 * constructor - creates a new rating for a movie but does not specify who the rating is from
	 * @param rating
	 * @param movie
	 */
	public Rating(final double rating, final Movie movie){
		this.rating = rating;
		this.movie = movie;
		profile = null;
	}



	/**@return the profile who gave the rating
	 */
	public Profile getProfile() {
		return profile;
	}


	/**@param profile - set the profile who gave the rating
	 */
	public void setProfile(final Profile profile) {
		this.profile = profile;
	}

	/**@return the movie that received this rating
	 */
	public Movie getMovie() {
		return movie;
	}

	/**@return  the (actual) rating that the profile assigned to the movie.
	 */
	public double getRating() {
		return rating;
	}

	@Override
	public String toString(){
		return profile.getUserId() + "\t" + movie.getId() + "\t" + rating + "\t" + 0l;
	}

}
