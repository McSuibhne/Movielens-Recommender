package ucd.ai.cf;

import java.util.Set;

/**
 * Main runnable class, demonstrates functionality of the different
 * collaborative filtering approaches available in the application.*/
public class Main {
    public static void main(String[] args) throws Exception {
        //allPairValues();
        //allPredictedRatings();
        meanAbsoluteError();
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

    /*Method prints out all predicted ratings for each user profile for each movie, based on MeanSquaredDifference and Pearson*/
    private static void allPredictedRatings() throws Exception{
        DatasetReader reader = new DatasetReader("MovieLens/u.item", "MovieLens/u.data_100");

        //loading the evaluation profiles instead of all profiles
        reader.loadEvaluationProfiles(0.2);
        Set<Profile> profiles = reader.getProfiles();

        MeanSquaredDifference msd = new MeanSquaredDifference(profiles);
        Pearson pearson = new Pearson(profiles);

        for (Profile profile: profiles) {
            for (Movie m : profile.getTargetMovieList()) {
                double msdPredictedRating = msd.predictRating(profile, m, 0.0);
                double pearsonPredictedRating = pearson.predictRating(profile, m, 0.0);
                System.out.print(profile.getUserId() + "\t" + profile.getTargetRating(m) + "\t");
                System.out.print(m.getName() + "\t");
                System.out.print("MSD_prediction=" + msdPredictedRating + "\t");
                System.out.println("Pearson_prediction=" + pearsonPredictedRating);
            }
        }
    }

    /**Method prints the Standard Deviation, Mean Absolute Error, and the Percentage Recommended (system coverage) using
     * MeanSquaredDifference with a threshold of 0.0, 0.5, 0.75 and 0.95,
     * and using Pearson with thresholds of 0.0, 0.25, 0.5 and 0.75*/
    static void meanAbsoluteError() throws Exception{
        //***DatasetReader reader = new DatasetReader("MovieLens/u.item", "MovieLens/u-filtered.data");
        DatasetReader reader = new DatasetReader("MovieLens/u.item", "MovieLens/u.data_100");
        reader.loadEvaluationProfiles(0.2);
        Set<Profile> profiles = reader.getProfiles();

        //MSD L=0.0;0.5;0.75;0.95
        MeanSquaredDifference msd = new MeanSquaredDifference(profiles);
        Evaluation evalMSD = new Evaluation(msd);
        System.out.println ("\t\tMAE\tPercentage Recommended");
        System.out.println ("MSD L=0.0" + "\t" + evalMSD.getStdDeviationError(0.0) + "\t" + evalMSD.getMeanAbsoluteError(0.0) + "\t" + evalMSD.getPercentageRecommended(0.0));
        System.out.println ("MSD L=0.5" + "\t" + evalMSD.getStdDeviationError(0.5) + "\t" + evalMSD.getMeanAbsoluteError(0.5) + "\t" + evalMSD.getPercentageRecommended(0.5));
        System.out.println ("MSD L=0.75" + "\t" + evalMSD.getStdDeviationError(0.75) + "\t" + evalMSD.getMeanAbsoluteError(0.75) + "\t" + evalMSD.getPercentageRecommended(0.75));
        System.out.println ("MSD L=0.95" + "\t" + evalMSD.getStdDeviationError(0.95) + "\t" + evalMSD.getMeanAbsoluteError(0.95) + "\t" + evalMSD.getPercentageRecommended(0.95));
        System.out.println ("");
        evalMSD.getMeanAbsoluteErrorDist(0.0);
        System.out.println ("");

        //Pearson L=0.0;0.25;0.5;0.75;
        Pearson pearson = new Pearson(profiles);
        Evaluation evalPearson = new Evaluation(pearson);
        System.out.println ("\t\tMAE\tPercentage Recommended");
        System.out.println ("Pearson L=0.0" + "\t" + evalPearson.getStdDeviationError(0.0) + "\t" + evalPearson.getMeanAbsoluteError(0.0) + "\t" + evalPearson.getPercentageRecommended(0.0));
        System.out.println ("Pearson L=0.25" + "\t" + evalPearson.getStdDeviationError(0.25) + "\t" + evalPearson.getMeanAbsoluteError(0.25) + "\t" + evalPearson.getPercentageRecommended(0.25));
        System.out.println ("Pearson L=0.5" + "\t" + evalPearson.getStdDeviationError(0.5) + "\t" + evalPearson.getMeanAbsoluteError(0.5) + "\t" + evalPearson.getPercentageRecommended(0.5));
        System.out.println ("Pearson L=0.75" + "\t" + evalPearson.getStdDeviationError(0.75) + "\t" + evalPearson.getMeanAbsoluteError(0.75) + "\t" + evalPearson.getPercentageRecommended(0.75));
        System.out.println ("");
        evalPearson.getMeanAbsoluteErrorDist(0.0);


    }
}