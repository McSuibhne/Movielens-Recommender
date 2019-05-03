package ucd.ai.cf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class contains all the ratings that a user has made on movies
 */
public class Profile {


	/**
	 * Returns the unique numeric id for this user
	 * @return
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * Returns the number of ratings in this profile
	 * @return
	 */
	public int size(){
		return allRatingsMap.size();
	}

	/** Returns the Rating that this user has given for given movie
	 * 
	 * @param movie The movie for which the rating is sought
	 * @return the rating value for the movie
	 */
	public double getRatingFor(final Movie movie) {
		Rating rating = allRatingsMap.get(movie);
		if(rating == null){
			return 0;
		}
		return rating.getRating();
	}


	private Integer userId;
	private Map<Movie, Rating> allRatingsMap;//the actual ratings given
	private Map<Movie, Rating> targetRatingsMap;//the ratings to predict

	public static int INSTANCE_COUNT = 0;
	private int internalID;

	public Profile(final Integer uid){
		this.userId = uid;
		allRatingsMap = new LinkedHashMap<Movie, Rating>();
		targetRatingsMap = new LinkedHashMap<Movie, Rating>();
		internalID = INSTANCE_COUNT;
		INSTANCE_COUNT++;
	}

	protected int internalID(){
		return this.internalID;
	}


	

}
