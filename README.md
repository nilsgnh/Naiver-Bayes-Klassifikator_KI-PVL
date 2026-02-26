# PVL Künstliche Intelligenz

## Benutzung
* Ausgangspunkt ist das Basisverzeichnis `KI-PVL_s85449`
* Kompilieren mit dem Befehl `javac -d bin src/*.java`, ausgeführt im Basisverzeichnis (KI-PVL_s85449)
* Starten des Programms durch `cd bin` und anschließend `java NaiveBayesClassifier <traindataA> <traindataB> <data_to_be_classified> [includeWordsNotInDataToBeClassified]`
    - *traindataA*: relative Pfadangabe (ausgehend vom Verzeichnis KI-PVL_s85449) zu einem txt-Dokument, welches alle Trainingsdaten für Klasse A beinhaltet (z.B.: data/train/A.txt)
    - *traindataB*: relative Pfadangabe (ausgehend vom Verzeichnis KI-PVL_s85449) zu einem txt-Dokument, welches alle Trainingsdaten für Klasse B beinhaltet (z.B.: data/train/B.txt)
    - *data_to_be_classified*: relative Pfadangabe (ausgehend vom Verzeichnis KI-PVL_s85449) zu einem txt-Dokument, welches die zu klassifizierenden Daten enthält
    - *[includeWordsNotInDataToBeClassified]*: *(optional)* Boolean-Wert, welcher bestimmt, ob
    alle Wörter des Trainingsvokabulars, auch wenn sie nicht in dem zu klassifizierendem Doku-
    ment vorkommen, in der zu berechnenden Wahrscheinlichkeit (als Gegenwahrscheinlichkeiten)
    berücksichtigt werden sollen. (**true** = Berücksichtigung von Wörtern, die nicht im zu klassifizierenden Doku-
    ment vorkommen, **false** = nur Berücksichtigung von Wörtern, welche im klas-
    sifizierenden Dokument vorkommen). Falls kein 4. Kommandozeilenparameter eingegeben
    wird, gilt hierfür standardmäßig false.

    - Der Aufruf mit den gegebenen Beispieldaten (siehe "Aufbau der Dateien/Beispieldaten") lautet somit `java NaiveBayesClassifier data/train/A.txt data/train/B.txt data/test.txt [true]`

## Aufbau der Dateien, welche Trainingsdaten oder die zu klassifizierenden Daten enthalten
### Trainingsdaten
* Die beiden Dateien für Klasse A und B sollten eine txt-Datei sein. Ansonsten sind auch weitere Textdateiformate möglich, solange
die Datei im Textformat vorliegt und durch Zeilen getrennte "Dokumente" beinhaltet.
* Jede Datei steht für eine Klasse und enthält pro Zeile ein Trainingsdatum/-dokument
* Zeilen werden durch einen Zeilenumbruch getrennt
### Klassifizierende Daten
* Die Datei sollte eine txt-Datei sein. Ansonsten sind auch weitere Textdateiformate möglich, solange
die Datei im Textformat vorliegt und durch Zeilen getrennte "Dokumente" beinhaltet.
* Diese Datei beinhaltet beliebige Zeilen an zu klassifizierenden Dokumenten
* Dabei wird jede Zeile als einzelnes Dokument klassifiziert
* Zeilen werden durch einen Zeilenumbruch getrennt

### Beispieldaten
Im Ordner *data* befinden sich Beispieldateien mit Beispieldaten aus Aufgabe 25 des KI-Praktikums.
* Trainingsdaten: *data/train/A.txt* oder *data/train/B.txt*
* Dokumente, die klassifiziert werden sollen: *data/test.txt*

## Verzeichnis- und Dateistruktur
* `docs`: Hier befindet sich die Dokumentation der PVL, in welcher ich auf die Benutzung des Programms und die Klasse inkl. ihrer Methoden (Architektur) eingehe.
* `src`: Hier befindet sich das gesamte Programm in Form von Java-Dateien, welches als Bernoulli Naive Bayes Klassifikator mit Laplace-Glättung fungiert. Mit übergebenenen Trainingsdaten können weitere Dokumente durch dieses Programm klassifiziert werden.
* `bin`: Enthält die kompilierte Klassendatei (.class) der Java-Quellcodedatei (.java), die sich im src-Ordner befindet. Diese ist mittels *java Klassendateiname [Kommandozeilenparameter]* ausführbar.
* `data`: Enthält bereits Beispielsdaten für Trainingsdaten für Klasse A und B (im Unterverzeichnis train) und eine Datei mit 2 zu klassifizierenden Dokumenten. Diese Beispieldaten entstammen der Aufgabe 25 aus dem KI-Prakiktum.