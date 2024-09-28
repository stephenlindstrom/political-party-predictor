import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class PoliticalPartyPredictor {
    public Map<String, Integer> partyCounts = new HashMap<>();
    public Map<String, Map<String, Map<String, Integer>>> responseCounts = new HashMap<>();
    public Map<String, Integer> responseTotals = new HashMap<>();
    public int totalSurveys = 0;

    // public void train(String[][] data, String[] partyAffiliations, String[] questions) {
    //     for (int i = 0; i < data.length; i++) {
    //         String partyAffiliation = partyAffiliations[i];
    //         partyCounts.put(partyAffiliation, partyCounts.getOrDefault(partyAffiliation, 0) + 1);
    //         totalSurveys++;

    //         for (int j = 0; j < data[i].length; j++) {
    //             String question = questions[j];
    //             String response = data[i][j];
    //             responseTotals.put(response, responseTotals.getOrDefault(response, 0) + 1);
    //             responseCounts.computeIfAbsent(partyAffiliation, k -> new HashMap<>()).computeIfAbsent(question, k -> new HashMap<>()).put(response, responseCounts.get(partyAffiliation).get(question).getOrDefault(response, 0) + 1);
    //         }
    //     }
    // }

    public void train(ArrayList<String[]> data, String partyAffiliation, String[] questions) {
        for (int i = 0; i < data.size(); i++) {
            partyCounts.put(partyAffiliation, partyCounts.getOrDefault(partyAffiliation, 0) + 1);
            totalSurveys++;

            for (int j = 0; j < data.get(i).length; j++) {
                String question = questions[j];
                String response = data.get(i)[j];
                responseTotals.put(response, responseTotals.getOrDefault(response, 0) + 1);
                responseCounts.computeIfAbsent(partyAffiliation, k -> new HashMap<>()).computeIfAbsent(question, k -> new HashMap<>()).put(response, responseCounts.get(partyAffiliation).get(question).getOrDefault(response, 0) + 1);
            }
        }
    }

    public String classify(String[] survey, String[] questions) {
        double bestProb = Double.NEGATIVE_INFINITY;
        String bestParty = null;

        for (String partyAffiliation : partyCounts.keySet()) {
            double logProb = Math.log(partyCounts.get(partyAffiliation) / (double) totalSurveys);

            for (int i = 0; i < survey.length; i++) {
                String question = questions[i];
                String response = survey[i];
                Map<String, Integer> answerCounts = responseCounts.getOrDefault(partyAffiliation, new HashMap<>()).getOrDefault(question, new HashMap<>());
                int answerCount = answerCounts.getOrDefault(response, 0);
                logProb += Math.log((answerCount + 1) / (double) (partyCounts.get(partyAffiliation) + answerCounts.size()));
            }

            if (logProb > bestProb) {
                bestProb = logProb;
                bestParty = partyAffiliation;
            }
        }

        return bestParty;
    }

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
            System.out.println("An error occurred");
            e.printStackTrace();
        }
        return surveyAnswers;
    } 
}