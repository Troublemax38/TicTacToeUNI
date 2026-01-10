package tictactoe.spieler;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;
import tictactoe.*;
import tictactoe.spieler.beispiel.Zufallsspieler;

import java.util.Random;
import java.util.ArrayList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static tictactoe.Farbe.*;

public class ReinforcementSpieler implements ILernenderSpieler {
    Farbe farbe;
    String name;

    //Properties / Konstanten für Persistenz
    // Hinweis: Netz-Datei und DataSet-Datei sind getrennt.
    String netPath = "F:\\TicTacToeUNI\\tictactoe.nnet";
    String trainingSetPath = "F:\\TicTacToeUNI\\tictactoe_training.tset";

    // Optional geladenes/gesammeltes DataSet
    private DataSet trainingSet;

    MultiLayerPerceptron multiLayerPerceptron =
            new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 9, 18, 9);

    double[] spielfeld = new double[9];
    double epsilon = 0;
    int letzterZug;

    Spielfeld feld = new Spielfeld();
    Spielfeld letzterZustand;

    private static final Random rnd = new Random();

    public ReinforcementSpieler(String name){
        this.name = name;

        // Learn() darf nicht "ewig" laufen -> Lernregel begrenzen
        // Für RL/Online-Lernen: sehr wenige Iterationen pro learn()-Aufruf.
        // Werte sind anpassbar (z.B. 5..50).
        try {
            multiLayerPerceptron.getLearningRule().setMaxIterations(10);
            multiLayerPerceptron.getLearningRule().setLearningRate(0.1);
        } catch (Exception ignored) {

        }

        // Netz laden, falls vorhanden
        ladeNetzWennVorhanden();

        // DataSet laden, falls vorhanden
        ladeTrainingSetWennVorhanden();

        //Nach Laden des Netzes erneut Parameter setzen (falls das geladene Netz andere Werte hatte)
        try {
            multiLayerPerceptron.getLearningRule().setMaxIterations(10);
            multiLayerPerceptron.getLearningRule().setLearningRate(0.1);
        } catch (Exception ignored) { }
    }

    @Override
    public Zug berechneZug(Zug vorherigerZug, long zeitKreuz, long zeitKreis) throws IllegalerZugException {
        if(vorherigerZug != null){
            // Spielfeld aktualisieren
            this.feld.setFarbe(vorherigerZug.getZeile(), vorherigerZug.getSpalte(), this.farbe.opposite());
            updateFeld(this.farbe.opposite(), (int) convertZugtoDouble(vorherigerZug));
        }

        int intZug = waehleZug(feld, epsilon);

        // Eigenen Zug intern setzen, damit der interne Zustand konsistent bleibt
        Zug eigenerZug = createZug(intZug);
        if (eigenerZug != null) {
            this.feld.setFarbe(eigenerZug.getZeile(), eigenerZug.getSpalte(), this.farbe);
            updateFeld(this.farbe, intZug);
        }

        return eigenerZug;
    }

    @Override
    public void neuesSpiel(Farbe meineFarbe, int bedenkzeitInSekunden) {
        setFarbe(meineFarbe);
        spielfeld = convertColorToInput(new Spielfeld());
        feld = new Spielfeld();
        letzterZustand = null;
        letzterZug = -1;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Farbe getFarbe() {
        return farbe;
    }

    @Override
    public void setFarbe(Farbe farbe) {
        this.farbe = farbe;
    }

    @Override
    public boolean trainieren(IAbbruchbedingung abbruchbedingung) throws IOException {
        ReinforcementSpieler player1 = new ReinforcementSpieler("Spieler1");
        ISpieler winner;

        player1.epsilon = 1;

        // Optional: Vortraining mit vorhandenem DataSet
        if (player1.trainingSet != null && !player1.trainingSet.isEmpty()) {
            player1.multiLayerPerceptron.learn(player1.trainingSet);
        }

        while(!abbruchbedingung.abbruch()) {
            TicTacToe game = new TicTacToe();
            try{
                //Es sollte zwischen Kreis und Kreuz immer wieder getauscht werden.
                //Nach genügend Training kann gegen einen weiteren ReinforcementSpieler gespielt werden.
                winner = game.neuesSpiel(player1 , new Zufallsspieler("Spieler2"), 0,false);
                System.out.println(winner);
            } catch (Exception e) {
                if(e.getClass().getName().contains("IllegalerZugException")){
                    double reward = checkReward(player1.feld, true, player1.farbe);
                    player1.trainNetz(player1.letzterZustand, player1.letzterZug, reward);
                }
                throw e;
            }

            double reward = checkReward(player1.feld, false, player1.farbe);

            player1.trainNetz(player1.letzterZustand, player1.letzterZug, reward);

            player1.epsilon = Math.max(0.05, player1.epsilon * 0.995);
        }

        // Netz speichern
        player1.speichereWissen(netPath);

        // DataSet speichern (wenn vorhanden/aktiv)
        if (player1.trainingSet != null) {
            player1.trainingSet.save(trainingSetPath);
        }

        // nach Training im Spielbetrieb greedy
        player1.epsilon = 0;
        this.multiLayerPerceptron = player1.multiLayerPerceptron;
        return abbruchbedingung.abbruch();
    }

    @Override
    public void speichereWissen(String filePath) throws IOException {
        this.multiLayerPerceptron.save(filePath);
    }

    @Override
    public void ladeWissen(String filePath) throws IOException {
        MultiLayerPerceptron geladen = (MultiLayerPerceptron) NeuralNetwork.createFromFile(filePath);
        setMultiLayerPerceptron(geladen);
    }

    private void setMultiLayerPerceptron(MultiLayerPerceptron trainingsSet) {
        this.multiLayerPerceptron = trainingsSet;
    }

    //Netz laden, falls Datei existiert
    private void ladeNetzWennVorhanden() {
        try {
            Path p = Path.of(netPath);
            if (Files.exists(p) && Files.isRegularFile(p)) {
                MultiLayerPerceptron geladen =
                        (MultiLayerPerceptron) NeuralNetwork.createFromFile(netPath);
                setMultiLayerPerceptron(geladen);
            } else {
                System.out.println("Kein bestehendes Netz gefunden. Starte mit neuem Netz.");
            }
        } catch (Exception e) {
            System.out.println("Netz konnte nicht geladen werden, starte mit neuem Netz: " + e.getMessage());
        }
    }

    private void ladeTrainingSetWennVorhanden() {
        try {
            Path p = Path.of(trainingSetPath);
            if (Files.exists(p) && Files.isRegularFile(p)) {
                trainingSet = DataSet.load(trainingSetPath);
            } else {
                trainingSet = null;
            }
        } catch (Exception e) {
            trainingSet = null;
        }
    }

    public double[] convertColorToInput(Spielfeld feld) {
        double[] farbFeld = new double[9];
        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                farbFeld[index] = feld.getFarbe(i, j).toInput();
                index++;
            }
        }
        return farbFeld;
    }

    public double convertZugtoDouble(Zug zug){
        switch (zug.getZeile()){
            case 0:
                if(zug.getSpalte() == 0) return 0;
                if(zug.getSpalte() == 1) return 1;
                if(zug.getSpalte() == 2) return 2;
            case 1:
                if(zug.getSpalte() == 0) return 3;
                if(zug.getSpalte() == 1) return 4;
                if(zug.getSpalte() == 2) return 5;
            case 2:
                if(zug.getSpalte() == 0) return 6;
                if(zug.getSpalte() == 1) return 7;
                if(zug.getSpalte() == 2) return 8;
        }
        return -1;
    }

    public double[] updateFeld(Farbe farbe, int feld){
        this.spielfeld[feld] = farbe.toInput();
        return this.spielfeld;
    }

    public int waehleZug(Spielfeld feld, double epsilon) throws IllegalerZugException {
        double[] output = evaluiereBoard(feld);

        ArrayList<Integer> verfuegbareZuege =  new ArrayList<>();
        for(int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if(feld.getFarbe(i,j) == Leer){
                    verfuegbareZuege.add((int) convertZugtoDouble(new Zug(i, j)));
                }
            }
        }
        if(verfuegbareZuege.isEmpty()){
            throw new IllegalerZugException();
        }

        // Exploration
        if(rnd.nextDouble() < epsilon){
            int randomZug = verfuegbareZuege.get(rnd.nextInt(verfuegbareZuege.size()));
            letzterZustand = feld.clone();
            letzterZug = randomZug;
            return randomZug;
        }

        int besterZug = -1;
        double besterWert = -Double.MAX_VALUE;

        for(int zugIndex : verfuegbareZuege){
            if(output[zugIndex] > besterWert){
                besterWert = output[zugIndex];
                besterZug = zugIndex;
            }
        }


        if (!verfuegbareZuege.contains(besterZug)) {
            besterZug = verfuegbareZuege.get(rnd.nextInt(verfuegbareZuege.size()));
        }

        letzterZustand = feld.clone();
        letzterZug = besterZug;

        return besterZug;
    }

    public double[] evaluiereBoard(Spielfeld feld){
        double[] input = convertColorToInput(feld);

        multiLayerPerceptron.setInput(input);
        multiLayerPerceptron.calculate();

        return multiLayerPerceptron.getOutput();
    }

    public double checkReward(Spielfeld feld, boolean illegalMove, Farbe spieler){
        if(illegalMove){
            return -1;
        }
        if(feld.pruefeGewinn(spieler) == Spielstand.GEWONNEN){
            return 1;
        }
        else if(feld.pruefeGewinn(spieler) == Spielstand.VERLOREN){
            return -1;
        }
        else if(feld.pruefeGewinn(spieler) == Spielstand.UNENTSCHIEDEN){
            return 0.2;
        }
        return 0;
    }

    private void trainNetz(Spielfeld zustand, int zug, double reward) {
        if (zustand == null || zug < 0 || zug > 8) {
            return;
        }

        double[] input = convertColorToInput(zustand);

        multiLayerPerceptron.setInput(input);
        multiLayerPerceptron.calculate();
        double[] output = multiLayerPerceptron.getOutput();

        double[] target = output.clone();

        // Maskierung belegter Felder (verhindert, dass belegte Felder "gut" werden)
        for (int i = 0; i < 9; i++) {
            if (zustand.getFarbe(i / 3, i % 3) != Leer) {
                target[i] = -1.0;
            }
        }

        target[zug] = reward;

        DataSet oneStep = new DataSet(9, 9);
        oneStep.add(new DataSetRow(input, target));

        // learn() durch MaxIterations begrenzt
        multiLayerPerceptron.learn(oneStep);

        //Erfahrung ins persistente DataSet übernehmen
        if (trainingSet != null) {
            trainingSet.add(new DataSetRow(input, target));
        }
    }

    public Zug createZug(int feldNummer){
        return switch (feldNummer) {
            case 0 -> new Zug(0, 0);
            case 1 -> new Zug(0, 1);
            case 2 -> new Zug(0, 2);
            case 3 -> new Zug(1, 0);
            case 4 -> new Zug(1, 1);
            case 5 -> new Zug(1, 2);
            case 6 -> new Zug(2, 0);
            case 7 -> new Zug(2, 1);
            case 8 -> new Zug(2, 2);
            default -> null;
        };
    }
}