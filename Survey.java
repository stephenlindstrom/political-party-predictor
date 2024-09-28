import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Survey {
    Scanner scanner = new Scanner(System.in);

    String[] questions = {
        "How should funding be provided to schools?",
        "What are your views on climate change?",
        "How should guns be regulated in the United States?",
        "Can one's gender be different than the sex assigned at birth?",
        "How should the integrity of the voting system be upheld?",
        "How do we improve the economy?",
        "What should we do about the war between Ukraine and Russia?",
        "Should abortion be legal?",
        "How should we handle the war in Gaza?",
        "What should we do about our borders?"
    };

    String[][] answers = {
        {"A. Funding should follow the child", "B. Public school funding should be increased", "C. The government should not be funding schools"},
        {"A. The Earth has been warming and cooling since the beginning of time", "B. It is our moral obligation to protect the environment for future generations", "C. It is the greatest threat to humanity in our history"},
        {"A. Guns are an important check on government overreach", "B. Limiting gun access is the best way to reduce gun violence", "C. Universal background checks should be required"},
        {"A. Yes", "B. No", "C. In rare cases"},
        {"A. Increase election security to reduce voter fraud", "B. Remove obstacles to voting to allow the people’s voice to be heard", "C. Make election day a national holiday"},
        {"A. Reduce regulations, government spending, and taxes", "B. Provide equal economic opportunity and social safety nets", "C. Implement community-based economics with worker ownership and control"},
        {"A. Financially support Ukraine against Russia", "B. Focus on issues in the US such as securing our border", "C. Maintain economic sanctions on Russia"}, 
        {"A. Yes", "B. No", "C. Only in cases of incest or rape"},
        {"A. Support our longtime ally, Israel", "B. Pressure for an immediate, permanent ceasefire", "C. Provide humanitarian aid but not military aid"}, 
        {"A. Increase border security and deportations", "B. Reduce barriers to entry and decrease deportations", "C. Become tougher on illegal immigration, but broaden avenues for legal immigration"}
    };

    public String[] takeSurvey() {
        String[] surveyAnswers = new String[10];
        for (int i = 0; i < questions.length; i++) {
            String questionNum = String.valueOf(i+1);
            System.out.println(questionNum + ". " + questions[i]);
            for (int j = 0; j < answers[i].length; j++) {
                System.out.println("    " + answers[i][j]);
            }
            System.out.print("Answer: ");
            surveyAnswers[i] = scanner.nextLine();
            while (!"A".equals(surveyAnswers[i]) && !"B".equals(surveyAnswers[i]) && !"C".equals(surveyAnswers[i])) {
                System.out.println("Please enter either 'A', 'B', or 'C'");
                System.out.print("Answer: ");
                surveyAnswers[i] = scanner.nextLine();
            }
            System.out.println();
        }
        System.out.println();
        return surveyAnswers;
    }

    public String getPartyAffiliation() {
        System.out.println("What party do you belong to? ");
        System.out.println("    " + "A. Republican");
        System.out.println("    " + "B. Democratic");
        System.out.println("    " + "C. Libertarian");
        System.out.println("    " + "D. Green");
        System.out.print("Answer: ");
        String partyAffiliation = scanner.nextLine();
        while (!"A".equals(partyAffiliation) && !"B".equals(partyAffiliation) && !"C".equals(partyAffiliation) && !"D".equals(partyAffiliation)) {
            System.out.println("Please enter either 'A', 'B', 'C', or 'D'");
            System.out.print("Answer: ");
            partyAffiliation = scanner.nextLine();
        }
        return partyAffiliation;
    }

    public void addSurveyData(String[] surveyAnswers, String party) {
        String partyAffiliation;
        if ("A".equals(party)) {
            partyAffiliation = "Republican";
        }
        else if ("B".equals(party)) {
            partyAffiliation = "Democratic";
        } 
        else if ("C".equals(party)) {
            partyAffiliation = "Libertarian";
        }
        else if ("D".equals(party)) {
            partyAffiliation = "Green";
        }
        else {
            throw new NoSuchElementException("Party not found");
        }

        try {
            FileWriter myWriter = new FileWriter("Party-Data/" + partyAffiliation + ".txt", true);
            for (String answer : surveyAnswers) {
                myWriter.write(answer);
            }
            myWriter.write("\n");
            myWriter.close();
        } 
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}