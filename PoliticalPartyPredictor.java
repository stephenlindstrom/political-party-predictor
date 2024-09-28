import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class PoliticalPartyPredictor {
    public Map<String, Integer> partyCounts = new HashMap<>();
    public Map<String, Map<String, Map<String, Integer>>> responseCounts = new HashMap<>();
    public int totalSurveys = 0;

    /* 
    Method that trains the model by counting the totals necessary to 
    calculate the posterior probability using Bayes Theorem. These totals 
    include the total number of surveys in all of the datasets used to 
    train the model, the total number of survey participants by party, 
    and the number of times each answer was chosen for each question based
    on the party.
    */
    public void train(ArrayList<String[]> data, String partyAffiliation, String[] questions) {
        for (int i = 0; i < data.size(); i++) {
            partyCounts.put(partyAffiliation, partyCounts.getOrDefault(partyAffiliation, 0) + 1);
            totalSurveys++;

            for (int j = 0; j < data.get(i).length; j++) {
                String question = questions[j];
                String response = data.get(i)[j];
                responseCounts.computeIfAbsent(partyAffiliation, k -> new HashMap<>()).computeIfAbsent(question, k -> new HashMap<>()).put(response, responseCounts.get(partyAffiliation).get(question).getOrDefault(response, 0) + 1);
            }
        }
    }

    /*
    Method that uses Bayes Theorem to calculate the probability that
    given the survey answers provided a survey participant belongs to 
    a certain party. Method returns party with the highest probability.
    Logarithms are used to calculate the probabilities to simplify the
    arithmetic to addition instead of multiplication. This also prevents 
    underflow since multiplying probabilities between 0 and 1 can lead to 
    very small numbers. 
     */
    public String classify(String[] survey, String[] questions) {
        // Since using logarithms, probabilities will be less than 0 
        double bestProb = Double.NEGATIVE_INFINITY;
        String bestParty = null;

        for (String partyAffiliation : partyCounts.keySet()) {
            // Calculate prior probability before survey responses are considered
            double logProb = Math.log(partyCounts.get(partyAffiliation) / (double) totalSurveys);

            for (int i = 0; i < survey.length; i++) {
                String question = questions[i];
                String response = survey[i];
                Map<String, Integer> answerCounts = responseCounts.getOrDefault(partyAffiliation, new HashMap<>()).getOrDefault(question, new HashMap<>());
                int answerCount = answerCounts.getOrDefault(response, 0);
                // Laplace smoothing is performed to handle situation where an answer may not have occurred in the training dataset
                // 1 is added to make this situation a small probability instead of 0, which would cause an error
                // The number of possible answers is added to denominator to prevent total probability from exceeding 1
                logProb += Math.log((answerCount + 1) / (double) (partyCounts.get(partyAffiliation) + answerCounts.size()));
            }

            if (logProb > bestProb) {
                bestProb = logProb;
                bestParty = partyAffiliation;
            }
        }

        return bestParty;
    }

    /*
    Method that retrieves data file for given party and returns an ArrayList
    with each entry being an array of strings that correspond to answers to 
    an individual survey.
     */
    public ArrayList<String[]> getData(String party) {
        ArrayList<String[]> surveyAnswers = new ArrayList<String[]>();
        try {
            File myFile = new File("Party-Data/" + party + ".txt");
            Scanner myReader = new Scanner(myFile);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] answerArray = data.split("");
                surveyAnswers.add(answerArray);
            }
            myReader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found: Party-Data/" + party + ".txt");
            e.printStackTrace();
        }
        return surveyAnswers;
    } 
}