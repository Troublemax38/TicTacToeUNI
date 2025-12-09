package tictactoe;


import tictactoe.spieler.ISpieler;
import tictactoe.spieler.beispiel.Zufallsspieler;

/** Definiert das Spielfeld und enthält Methoden, um ein Spiel durchzuführen.
 * 
 * @author Jürgen Priemer
 *
 */
public class TicTacToe {
	
	private Spielfeld feld = new Spielfeld();
	
	private void neuesSpiel() {
		feld = new Spielfeld();
	}
	
	/**
	 * Erzeugt ein neues Spiel, das bis zum Ende durchgespielt wird.
	 * Zur Zugberechnung wird berechneZug(..) für den jeweiligen ISpieler aufgerufen.
	 * @param spielerKreuz (spielt Kreuz) und beginnt
	 * @param spielerKreis (spielt Kreis)
	 * @param bedenkzeitInSek (Gesamtbedenkzeit für jeden ISpieler)
	 * @param debug Wenn True wird das Spielfeld auf der Konsole ausgegeben
	 * @return ISpieler gewinner
	 */
	public ISpieler neuesSpiel(ISpieler spielerKreuz, ISpieler spielerKreis, int bedenkzeitInSek, boolean debug) {
		neuesSpiel();
		spielerKreuz.neuesSpiel(Farbe.Kreuz, bedenkzeitInSek);
		spielerKreis.neuesSpiel(Farbe.Kreis, bedenkzeitInSek);
		try {
			Zug vorherigerZug = null;
		while (true) {
			//1. ISpieler am Zug
			vorherigerZug = spielerKreuz.berechneZug(vorherigerZug,0,0); //TODO: Zeitsteuerung einbauen!
			Spielstand stand = machZug(spielerKreuz.getFarbe(), vorherigerZug);
			if (debug) druckeBrett();
			if (stand == Spielstand.GEWONNEN) return spielerKreuz;
			if (stand == Spielstand.UNENTSCHIEDEN) return null;

			//2. ISpieler am Zug
			vorherigerZug = spielerKreis.berechneZug(vorherigerZug,0,0); //TODO: Zeitsteuerung einbauen!
			stand = machZug(spielerKreis.getFarbe(), vorherigerZug);
			if (debug) druckeBrett();
			if (stand == Spielstand.GEWONNEN) return spielerKreis;
			if (stand == Spielstand.UNENTSCHIEDEN) return null;
		}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Durchführen eines Zuges für den ISpieler mit der angegebenen Farbe
	 * @param spieler: Farbe des Spielers am Zug
	 * @param zug
	 * @return aktueller Spielstand
	 * @throws IllegalerZugException
	 */
	private Spielstand machZug(Farbe spieler, Zug zug) throws IllegalerZugException {
		if (feld.getFarbe(zug.getZeile(),zug.getSpalte()) == Farbe.Leer) {
			//Alles in Ordnung: Zug machen und auf Gewinn prüfen
			feld.setFarbe(zug.getZeile(),zug.getSpalte(), spieler);
			return feld.pruefeGewinn(spieler);
		}
		else {
			throw new IllegalerZugException();
		}
	}
	
	/** Gibt das Brett auf dem Bildschirm aus. */
	public void druckeBrett() {
        System.out.println("_____________");
		for (int zeile=0;zeile<3;zeile++) {
	         System.out.println("|   |   |   |");
	         System.out.println("| " + this.feld.getFarbe(zeile, 0) + " | " + this.feld.getFarbe(zeile, 1) + " | " + this.feld.getFarbe(zeile, 2) + " |");
	         System.out.println("|___|___|___|");
		}
		System.out.println();
	}

	/** Nur für Testzwecke */
	public static void main(String[] args) {
		TicTacToe ttt = new TicTacToe();
		ISpieler spieler1 = new Zufallsspieler("Spieler1");
		ISpieler spieler2 = new Zufallsspieler("Spieler2");
		
		ISpieler gewinner = ttt.neuesSpiel(spieler1, spieler2, 150, true); //150 Sekunden Gesamtbedenkzeit pro ISpieler
		if (gewinner == null) {
			System.out.println("Unentschieden!");
		}
		else {
			System.out.println("Gewonnen hat: " + gewinner.getName());
		}
	}
}
