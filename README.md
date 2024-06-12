# KI-PVL
## Daten von Belegenden
* Name: Nils Generlich
* Bibliotheksnummer: s85449
* Matrikelnummer: 53047
* Studiengruppe: 22/041/62

## Benutzung
* Kompilieren mit Aufruf von `./make.sh` oder dem Befehl `javac -d bin src/*.java`
* Starten des Programms durch `cd bin` und anschließend `java NaiveBayesClassifier <traindataA> <traindataB> <data_to_be_classified>`
    - *traindataA*: relative Pfadangabe (ausgehend vom Verzeichnis KI-PVL) zu einem txt-Dokument, welches alle Trainingsdaten für Klasse A beinhaltet (z.B.: data/train/A.txt)
    - *traindataB*: relative Pfadangabe (ausgehend vom Verzeichnis KI-PVL) zu einem txt-Dokument, welches alle Trainingsdaten für Klasse B beinhaltet (z.B.: data/train/B.txt)
    - *data_to_be_classified*: relative Pfadangabe (ausgehend vom Verzeichnis KI-PVL) zu einem txt-Dokument, welches die zu klassifizierenden Daten enthält
    - Der Aufruf mit den gegebenen Beispieldaten lautet somit `java NaiveBayesClassifier data/train/A.txt data/train/B.txt data/test.txt`

## Aufbau der Dateien, welche Trainingsdaten oder die zu klassifizierenden Daten enthalten
### Trainingsdaten
* Die Datei muss eine txt-Datei (Endung .txt) sein
* Jede Datei steht für eine Klasse und enthält pro Zeile ein Trainingsdatum/-dokument
* Zeilen werden durch einen Zeilenumbruch (\n für Linux/MacOS oder \r\n für Windows) getrennt
### Klassifizierende Daten
* Die Datei muss eine txt-Datei (Endung .txt) sein
* Diese Datei beinhaltet beliebige Zeilen an zu klassifizierenden Dokumenten
* Dabei wird jede Zeile als einzelnes Datum klassifiziert
* Zeilen werden durch einen Zeilenumbruch (\n für Linux/MacOS oder \r\n für Windows) getrennt

### Beispieldaten
Im Ordner *data* befinden sich Beispieldateien mit Beispieldaten aus der Vorlesung.
* Trainingsdaten: *data/train/A.txt* oder *data/train/B.txt*
* Daten, die klassifiziert werden sollen: *data/test.txt*

## Verzeichnis- und Dateistruktur
