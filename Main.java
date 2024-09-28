import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String[] questions = {"Education", "Climate", "Gun Control", "Gender", "Election Integrity", "Economy", "Ukraine", "Abortion", "Gaza", "Immigration"};

        PoliticalPartyPredictor ppp = new PoliticalPartyPredictor();
        
        ArrayList<String[]> republicanData = ppp.getData("Republican");
        ppp.train(republicanData, "Republican", questions);

        ArrayList<String[]> democraticData = ppp.getData("Democratic");
        ppp.train(democraticData, "Democratic", questions);

        ArrayList<String[]> libertarianData = ppp.getData("Libertarian");
        ppp.train(libertarianData, "Libertarian", questions);

        ArrayList<String[]> greenData = ppp.getData("Green");
        ppp.train(greenData, "Green", questions);

        Survey politicalSurvey = new Survey();
        String[] surveyAnswers = politicalSurvey.takeSurvey();

        String predictedParty = ppp.classify(surveyAnswers, questions);
        System.out.println("I'm guessing you belong to the " + predictedParty + " party");

        String partyAffiliation = politicalSurvey.getPartyAffiliation();
        politicalSurvey.addSurveyData(surveyAnswers, partyAffiliation);    
    }


}