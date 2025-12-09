package tictactoe.spieler;

import tictactoe.Farbe;
import tictactoe.IllegalerZugException;
import tictactoe.Zug;

/** Schnittstelle für alle ISpieler.
 * @author Jürgen Priemer
 */
public interface ISpieler {
	
	/**
	 * Ein neues Spiel wird gestartet. Der ISpieler muss schon vorher trainiert worden sein!
	 * @param meineFarbe
	 * @param bedenkzeitInSekunden
	 */
	public void neuesSpiel(Farbe meineFarbe, int bedenkzeitInSekunden);
	
	/**
	 * @return Bezeichnung des eigenen Spielers
	 */
	public String getName();
	
	/**
	 * 
	 * @param name Name des Spielers
	 */
	public void setName(String name);
	
	/**
	 * @return eigene Farbe
	 */
	public Farbe getFarbe();
	
	/**
	 * Eigene Farbe setzen.
	 * @param farbe
	 */
	public void setFarbe(Farbe farbe);

	/**
	 * Diese Methode muss zur Zugberechnung implementiert werden
	 * @param vorherigerZug: Der letzte Zug des Gegners. null falls der Gegner vorher keinen Zug gemacht hat
	 * @param zeitKreuz: Bisher verbrauchte Zeit des Spielers mit Farbe Kreis in Millisekunden
	 * @param zeitKreis: Bisher verbrauchte Zeit des Spielers mit Farbe Kreuz in Millisekunden
	 * @return berechneten Zug
	 * @throws IllegalerZugException wenn vorherigerZug illegal ist (nach Meinung des Spielers)
	 */
	public Zug berechneZug(Zug vorherigerZug, long zeitKreuz, long zeitKreis) throws IllegalerZugException;
}

