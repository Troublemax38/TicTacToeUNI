## Disclaimer

Dieses Projekt wurde im Rahmen einer universitären Lehrveranstaltung erstellt und dient ausschließlich akademischen und didaktischen Zwecken.

Die Implementierung stellt einen experimentellen Ansatz dar und erhebt keinen Anspruch auf Vollständigkeit, Korrektheit oder Produktivfähigkeit. Der Code wurde primär zur Demonstration von Lernverfahren entwickelt.

Die Nutzung erfolgt auf eigene Verantwortung.

## Nutzung des Programms

Bevor das Programm genutzt werden kann, muss in der Klasse "ReinforcementSpieler.java" ein zulässiger Dateipfad zum Speichern und Laden des neuronalen Netzes, sowie des Data-Sets deklariert werden.
Die Klasse wird in dem folgenden Verzeichnis gefunden: "src/tictactoe/spieler/ReinforcementSpieler.java"
Dazu müssen die Variable "netPath" und "trainingSetPath" gesetzt werden.
Sobald dies gesetzt ist, kann die Klasse ReinforcementSpieler genutzt werden und es kann ein Wettkampf gestartet werden.

## Anpassungen vom Training
Je nach dem, ob das neuronale Netz auf Kreuz oder Kreis trainiert werden soll, muss die Reihenfolge der Spieler bei der Methode "trainieren" beim Methodenaufruf von "neuesSpiel" in ReinforcementSpieler in Zeile 136 angepasst werden.
Es kann hierbei auch ein weiterer ReinforcementSpieler genutzt werden. Dies liegt im Ermessen des Anwenders.

## Hinweis
Bitte beachten Sie die Kommentare am Code, fall etwas verändert wird.
