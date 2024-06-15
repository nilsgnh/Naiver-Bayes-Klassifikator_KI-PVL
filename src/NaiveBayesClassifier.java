import java.io.*;
import java.util.*;
import java.nio.file.*;

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

    public NaiveBayesClassifier() {
        this.vocabulary = new HashSet<>();
        this.wordCountA = new HashMap<>();
        this.wordCountB = new HashMap<>();
    }

    /* Trainiert den Naive Bayes Classifier mit den Trainingsdaten von Klasse A und B durch Aufruf von trainClass() */
    public void train(String filePathA, String filePathB) throws IOException {
        if(!trainClass(filePathA, wordCountA, true))
        {
            System.out.println("train(): Error beim Trainieren von Klasse A");
            return;
        }
        if(!trainClass(filePathB, wordCountB, false))
        {
            System.out.println("train(): Error beim Trainieren von Klasse B");
            return;
        }
    }

    /* Trainiert den Naive Bayes Classifier mit den Trainingsdaten von einer spezifischen Klasse (A oder B) */
    private boolean trainClass(String filePath, Map<String, Integer> wordCount, boolean classA){
        boolean wasAdded = true;
        List<String> lines = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = Files.newBufferedReader(Paths.get(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }

        if (classA) {
            totalLinesA = lines.size();
        } 
        else {
            totalLinesB = lines.size();
        }

        for(String line : lines){
            Set<String> uniqueWordsInLine = new HashSet<>(); //Set speichert nur einzigartige Werte
            String[] words = line.split("\\W+"); //Trennung des Strings bei einem oder mehreren Nicht-Wort-Zeichen (\W+)
            for (String word : words) {
                if (!word.isEmpty()) {
                    word = word.toLowerCase();
                    wasAdded=uniqueWordsInLine.add(word);
                    /*if (wasAdded) {
                        System.out.println("Das Wort " + word + " wurde zum Vokabular hinzugefügt.");
                    } else {
                        System.out.println("Das Wort " + word + " war bereits im Vokabular vorhanden.");
                    }*/
                    wasAdded = vocabulary.add(word);
                    /*if (wasAdded) {
                        System.out.println("Das Wort " + word + " wurde zum Vokabular hinzugefügt.");
                    } else {
                        System.out.println("Das Wort " + word + " war bereits im Vokabular vorhanden.");
                    }*/
                }
            }
            for (String word : uniqueWordsInLine) {
                wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
            }
        }
        return true;
    }

    /* Klassifiziert alle Dokumente (je eine Zeile) in einer Datei anhand der Trainingsdaten von Klasse A und B und gibt die 
    Klassifizierung aus */
    public void classify_all(String filePath, boolean includeWordsNotInTestData) throws IOException {
        int i=1;
        List<String> lines = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = Files.newBufferedReader(Paths.get(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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

        double totalLines = totalLinesA + totalLinesB;
        double probA = totalLinesA / totalLines;
        double probB = totalLinesB / totalLines;
        //System.out.println("prob(doc|A): " + probA);
        //System.out.println("prob(doc|B): " + probB);

        for (String word : uniqueWordsInDoc) {
            //wenn Wort im Vokabular enthalten, wird es in Berechnung einbezogen, ansonsten ignoriert
            if (vocabulary.contains(word)) {
                int countA = wordCountA.getOrDefault(word, 0); //bekommt den Wert des Schlüssels word, wenn Schlüssel nicht existiert = 0
                int countB = wordCountB.getOrDefault(word, 0);

                //System.out.println("Word: " + word);

                probA *= (countA + 1.0) / (totalLinesA + 2.0);
                probB *= (countB + 1.0) / (totalLinesB + 2.0);
                //System.out.println("(notfinal) prob(doc|A): " + probA);
                //System.out.println("(notfinal) prob(doc|B): " + probB);
                //System.out.println("-----------------------------");
            }
        }

        if(includeWordsNotInTestData){
            for(String word : vocabulary){
                if(!uniqueWordsInDoc.contains(word)){
                    int countA = wordCountA.getOrDefault(word, 0);
                    int countB = wordCountB.getOrDefault(word, 0);
                    //System.out.println("Word (not in doc): " + word);
                    probA *= 1-((countA + 1.0) / (totalLinesA + 2.0));
                    probB *= 1-((countB + 1.0) / (totalLinesB + 2.0));
                    //System.out.println("(notfinal) prob(doc|A): " + probA);
                    //System.out.println("(notfinal) prob(doc|B): " + probB);
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

        //Prüfung ob die Pfade zu den Trainingsdaten und dem File mit den zu klassifizierenden Dokumenten existieren
        if (!Files.exists(Paths.get(trainPathA))) {
            System.out.println("Die Datei " + args[0] + " existiert nicht.");
            return;
        }
        if (!Files.exists(Paths.get(trainPathB))) {
            System.out.println("Die Datei " + args[1] + " existiert nicht.");
            return;
        }
        if (!Files.exists(Paths.get(testFilePath))) {
            System.out.println("Die Datei " + args[2] + " existiert nicht.");
            return;
        }

        NaiveBayesClassifier classifier = new NaiveBayesClassifier();
        classifier.train(trainPathA, trainPathB); //trainiert den Classifier mit den Trainingsdaten von Klasse A und B
        classifier.classify_all(testFilePath, includeWordsNotInTestData); //ruft für jede Zeile des Files (jedes Dokument) classify() auf
    }
}