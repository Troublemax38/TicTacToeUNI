package tictactoe.spieler.beispiel;

import tictactoe.Farbe;
import tictactoe.IllegalerZugException;
import tictactoe.Zug;
import tictactoe.spieler.IAbbruchbedingung;
import tictactoe.spieler.ILernenderSpieler;

import java.io.IOException;

public class ReinforcementSpieler implements ILernenderSpieler {

    @Override
    public Zug berechneZug(Zug vorherigerZug, long zeitKreuz, long zeitKreis) throws IllegalerZugException {
        return null;
    }

    @Override
    public void neuesSpiel(Farbe meineFarbe, int bedenkzeitInSekunden) {

    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public Farbe getFarbe() {
        return null;
    }

    @Override
    public void setFarbe(Farbe farbe) {

    }


    @Override
    public boolean trainieren(IAbbruchbedingung abbruchbedingung) {
        return false;
    }

    @Override
    public void speichereWissen(String name) throws IOException {

    }

    @Override
    public void ladeWissen(String name) throws IOException {

    }
}
