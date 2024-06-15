import java.io.*;
import java.nio.file.*;
import java.util.*;

public class NaiveBayesClassifier {
    /*
    Klasse NaiveBayesClassifier implementiert einen Bernoulli Naiven Bayes Klassifizierer mit Laplace Glättung, der Dokumente auf Basis von 
    Trainingsdaten in eine von zwei Klassen (A,B) klassifiziert.
    */

    /*Menge aller Wörter, die in den Trainingsdaten von Klasse A oder B vorkommen */
    private Set<String> vocabulary; 
    /*Mapping mit Wort als Schlüssel und die Anzahl, wie oft das Wort in Klasse A/B vorkommt als Integer-Wert: */
    private Map<String, Integer> wordCountA; 
    private Map<String, Integer> wordCountB;
    /*Anzahl der Dokumente in Klasse A/B */
    private int totalLinesA;
    private int totalLinesB;
    /* Anzahl der Wörter in Klasse A/B */
    private int totalWordsA;
    private int totalWordsB;
    /* Anzahl der Wörter im Vokabular */
    private int vocabSize;
    /* Dateipfad der Trainingsdaten von Klasse A/B */
    private String fpA;
    private String fpB;

    public NaiveBayesClassifier() {
        this.vocabulary = new HashSet<>();
        this.wordCountA = new HashMap<>();
        this.wordCountB = new HashMap<>();
        this.totalWordsA = 0;
        this.totalWordsB = 0;
        this.vocabSize = 0;
    }

    /* Trainiert den Naive Bayes Classifier mit den Trainingsdaten von Klasse A und B durch Aufruf von trainClass() */
    public void train(String filePathA, String filePathB) throws IOException {
        this.fpA = filePathA;
        this.fpB = filePathB;
        if(!trainClass(filePathA, wordCountA, true))
        {
            System.out.println("train(): Error in training class A");
            return;
        }
        if(!trainClass(filePathB, wordCountB, false))
        {
            System.out.println("train(): Error in training class B");
            return;
        }
        this.vocabSize = vocabulary.size();
    }

    /* Trainiert den Naive Bayes Classifier mit den Trainingsdaten von einer spezifischen Klasse (A oder B) */
    private boolean trainClass(String filePath, Map<String, Integer> wordCount, boolean classA) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        if (classA) {
            totalLinesA = lines.size();
        } 
        else {
            totalLinesB = lines.size();
        }

        for(String line : lines){
            Set<String> uniqueWordsInLine = new HashSet<>(); //HashSet speichert nur einzigartige Werte
            String[] words = line.split("\\W+"); // \\W+ bedeutet, dass der String an einem oder mehreren nicht Wortzeichen getrennt wird
            for (String word : words) {
                if (!word.isEmpty()) {
                    word = word.toLowerCase();
                    uniqueWordsInLine.add(word);
                    vocabulary.add(word);
                }
            }
            for (String word : uniqueWordsInLine) {
                wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
            }
        }
        
        if (classA) {
            totalWordsA += wordCount.size();
        } else {
            totalWordsB += wordCount.size();
        }

        return true;
    }

    /* Klassifiziert alle Dokumente (je eine Zeile) in einer Datei anhand der Trainingsdaten von Klasse A und B und gibt die Klassifizierung aus */
    public void classify_all(String filePath, boolean includeWordsNotInTestData) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int i=1;
        System.out.println();
        System.out.println("Documents to be classified from file: " + filePath);
        for(String line : lines){
            System.out.println("-------------------------------------------------");
            System.out.println("Classification of document " + i + ":");
            System.out.println(classify(line, includeWordsNotInTestData, i));
            i++;
        }
        System.out.println();
    }

    /* Klassifiziert ein Dokument anhand der Trainingsdaten von Klasse A und B */
    public String classify(String line, boolean includeWordsNotInTestData, int i) throws IOException {
        Set<String> uniqueWordsInDoc = new HashSet<>();

        String[] words = line.split("\\W+");
        for (String word : words) {
            if (!word.isEmpty()) {
                uniqueWordsInDoc.add(word.toLowerCase());
            }
        }

        //System.out.println("total training data class A (aus "+ fpA +"): "  + totalLinesA);
        //System.out.println("total training data class B (aus "+ fpB +"): " + totalLinesB);
        double totalLines = totalLinesA + totalLinesB;
        double probA = totalLinesA / totalLines;
        //System.out.println("p(A): " + probA);
        double probB = totalLinesB / totalLines;
        //System.out.println("p(B): " + probB);

        //System.out.println("-----------------------------");
        for (String word : uniqueWordsInDoc) {
            //wenn das Wort im Vokabular enthalten ist, wird es in die Berechnung einbezogen
            if (vocabulary.contains(word)) {
                int countA = wordCountA.getOrDefault(word, 0); //bekommt den Wert des Schlüssels word, wenn Schlüssel nicht existiert = 0
                int countB = wordCountB.getOrDefault(word, 0);

                //System.out.println("Word: " + word);

                probA *= (countA + 1.0) / (totalLinesA + 2.0);
                probB *= (countB + 1.0) / (totalLinesB + 2.0);
                /*//System.out.println("countA+1: " + (countA+1));
                //System.out.println("totalLinesA+2: " + (totalLinesA+2));*/
                //System.out.println("(notfinal) prob(testdata|A): " + probA);
                //System.out.println("(notfinal) prob(testdata|B): " + probB);
                //System.out.println("-----------------------------");
            }
        }

        if(includeWordsNotInTestData){
            for(String word : vocabulary){
                if(!uniqueWordsInDoc.contains(word)){
                    int countA = wordCountA.getOrDefault(word, 0);
                    int countB = wordCountB.getOrDefault(word, 0);
                    //System.out.println("Word (not in testdata): " + word);
                    probA *= 1-((countA + 1.0) / (totalLinesA + 2.0));
                    probB *= 1-((countB + 1.0) / (totalLinesB + 2.0));
                    //System.out.println("(notfinal) prob(testdata|A): " + probA);
                    //System.out.println("(notfinal) prob(testdata|B): " + probB);
                    //System.out.println("-----------------------------");
                }
            }
        }

        if (probA > probB) {
            return "prob(doc" + i + "|A) = " + probA + "\nprob(doc" + i + "|B) = " + probB +"\nThe document " + i + 
            " was classified as class A with the probability: prob(doc" + i + "|A) = " + probA;
        } else {
            return "prob(doc" + i + "|A) = " + probA + "\nprob(doc" + i + "|B) = " + probB +"\nThe document " + i + 
            " was classified as class B with the probability: prob(doc" + i + "|B) = " + probB;
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 3 && args.length != 4) {
            System.out.println("Usage: java NaiveBayesClassifier <trainA> <trainB> <FileWithDocsToBeClassified> [includeWordsNotInDataToBeClassified]");
            return;
        }

        boolean includeWordsNotInTestData = false;
        if(args.length == 4 && args[3].equals("true")){
            includeWordsNotInTestData = true;
        }

        String trainPathA = "../"+args[0];
        String trainPathB = "../"+args[1];
        String testFilePath = "../"+args[2];

        NaiveBayesClassifier classifier = new NaiveBayesClassifier();
        classifier.train(trainPathA, trainPathB);
        classifier.classify_all(testFilePath, includeWordsNotInTestData); //für jede Zeile classify aufrufen!
    }
}