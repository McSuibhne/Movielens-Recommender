package ucd.ai.cf;


/**This represents a movie that the users can rate.
 */
public class Movie {
	private String name;
	private Integer id;

	public Movie(final String name, final Integer id){
		this.name = name;
		this.id = id;
	}

	/** @return the numeric ID for the movie*/
	public Integer getId() {
		return id;
	}

	/**@return The title for the movie*/
	public String getName() {
		return name;
	}
}
