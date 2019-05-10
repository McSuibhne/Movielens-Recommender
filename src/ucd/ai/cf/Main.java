package ucd.ai.cf;

import java.util.Set;

/**
 * Main runnable class, demonstrates functionality of the different
 * collaborative filtering approaches available in the application.*/
public class Main {
    public static void main(String[] args) throws Exception {
        allPairValues();
    }

    /*Method prints out the MeanSquaredDifference and the Pearson values for each possible pair of user profiles*/
    private static void allPairValues() throws Exception{
        DatasetReader ld = new DatasetReader("MovieLens/u.item", "MovieLens/u.data_100");
        ld.loadAllProfiles();
        Set<Profile> profiles = ld.getProfiles();

        MeanSquaredDifference msd = new MeanSquaredDifference(profiles);
        Pearson pearson = new Pearson(profiles);

        for (Profile a: profiles) {
            for (Profile b: profiles) {
                if (msd.computeSimilarity(a,b) > 0 && Math.abs(pearson.computeSimilarity(a,b)) > 0)
                    System.out.println(a.getUserId() + "\t" + b.getUserId() + "\tMSD=\t" + msd.computeSimilarity(a,b) + "\t\tPearson=\t" + pearson.computeSimilarity(a,b));
            }
        }
    }
}