import java.io.*;
import java.nio.file.*;
import java.util.*;

public class NaiveBayesClassifier {
    /*
    Klasse NaiveBayesClassifier implementiert einen Naive Bayes Classifier, der Dokumente auf Basis von 
    Trainingsdaten in eine von zwei Klassen klassifiziert.
        Methoden:
            train: Trainiert den Naive Bayes Classifier mit den Trainingsdaten von Klasse A und B
            classify: Klassifiziert ein Dokument anhand der Trainingsdaten von Klasse A und B
            classify_all: Klassifiziert alle Dokumente in einer Datei anhand der Trainingsdaten von Klasse A und B und gibt die Klassifizierung aus
        Klassenvariablen:
            vocabulary: Menge aller Wörter, die in den Trainingsdaten von Klasse A oder B vorkommen
            wordCountA: Mapping mit Wort als Schlüssel und die Anzahl, wie oft das Wort in Klasse A vorkommt als Integer-Wert
            wordCountB: Mapping mit Wort als Schlüssel und die Anzahl, wie oft das Wort in Klasse B vorkommt als Integer-Wert
            totalLinesA: Anzahl der Dokumente in Klasse A
            totalLinesB: Anzahl der Dokumente in Klasse B
            totalWordsA: Anzahl der Wörter in Klasse A
            totalWordsB: Anzahl der Wörter in Klasse B
            vocabSize: Anzahl der Wörter im Vokabular
            fpA: Dateipfad der Trainingsdaten von Klasse A
            fpB: Dateipfad der Trainingsdaten von Klasse B
     */
    private Set<String> vocabulary;
    private Map<String, Integer> wordCountA;
    private Map<String, Integer> wordCountB;
    private int totalLinesA;
    private int totalLinesB;
    private int totalWordsA;
    private int totalWordsB;
    private int vocabSize;
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

    public void train(String filePathA, String filePathB) throws IOException {
        this.fpA = filePathA;
        this.fpB = filePathB;
        if(!trainClass(filePathA, wordCountA))
        {
            System.out.println("train(): Error in training class A");
            return;
        }
        if(!trainClass(filePathB, wordCountB))
        {
            System.out.println("train(): Error in training class B");
            return;
        }
        this.vocabSize = vocabulary.size();
    }

    private boolean trainClass(String filePath, Map<String, Integer> wordCount) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        if (filePath.endsWith("A.txt")) {
            totalLinesA = lines.size();
        } 
        else if(filePath.endsWith("B.txt")) {
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
        
        if (filePath.endsWith("A.txt")) {
            totalWordsA += wordCount.size();
        } else if (filePath.endsWith("B.txt")) {
            totalWordsB += wordCount.size();
        }

        return true;
    }

    public void classify_all(String filePath, boolean includeWordsNotInTestData) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int i=1;
        System.out.println();
        System.out.println("Documents to be classified from file: " + filePath);
        for(String line : lines){
            System.out.println("-------------------------------------------------");
            System.out.println("Classification of document " + i + ":");
            System.out.println(classify(line, includeWordsNotInTestData));
            i++;
        }
        System.out.println();
    }

    public String classify(String line, boolean includeWordsNotInTestData) throws IOException {
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
                int countA = wordCountA.getOrDefault(word, 0);//bekommt den Wert des Schlüssels word, wenn der Schlüssel nicht existiert, wird 0 zurückgegeben
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
            return "prob(testdata|A) = " + probA + "\nprob(testdata|B) = " + probB +"\nThe test data was classified as class A with the probability: prob(testdata|A) = " + probA;
        } else {
            return "prob(testdata|A) = " + probA + "\nprob(testdata|B) = " + probB +"\nThe test data was classified as class B with the probability: prob(testdata|B) = " + probB;
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 3 && args.length != 4) {
            System.out.println("Usage: java NaiveBayesClassifier <trainA> <trainB> <testFile> [includeWordsNotInDataToBeClassified]");
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