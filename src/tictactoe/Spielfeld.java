package tictactoe;

/** Bildet ein TicTacToe-Spielfeld als 3x3 Matrix ab
 * Zeile 0..2, Spalte 0..2
 * @author Jürgen Priemer
 *
 */
public class Spielfeld implements Cloneable {
	
	private Farbe[][] feld = new Farbe[3][3];
	
	public Spielfeld() {
		for (int zeile=0; zeile<3; zeile++) 
			for (int spalte=0; spalte<3; spalte++)
				feld[zeile][spalte] = Farbe.Leer;
	}
	
	/**
	 * Kopie eines Spielfelds erzeugen.
	 */
	public Spielfeld clone() {
		Spielfeld kopie = new Spielfeld();
		for (int zeile=0; zeile<3; zeile++) 
			for (int spalte=0; spalte<3; spalte++)
				kopie.feld[zeile][spalte] = this.feld[zeile][spalte];
		return kopie;
	}
	
	public void setFarbe(int zeile, int spalte, Farbe farbe) {
		assert((zeile>=0) && (zeile<3) && (spalte>=0) && (spalte<3));
		feld[zeile][spalte]=farbe;
	}
	
	public Farbe getFarbe(int zeile, int spalte) {
		assert((zeile>=0) && (zeile<3) && (spalte>=0) && (spalte<3));
		return feld[zeile][spalte];
	}
	
	/** Prüft für ein gegebenes Spielbrett, ob die Farbe dort gewonnen hat
	 * 
	 * @param spieler
	 * @return
	 */
	public Spielstand pruefeGewinn(Farbe spieler) {
		// Prüft, ob eine Farbe gewonnen hat
		Farbe s = getFarbe(0,0); //unten links
		if (s != Farbe.Leer) {
			if ((getFarbe(0,1) == s) && (getFarbe(0,2) == s)) return spielende(spieler,s);
			if ((getFarbe(1,1) == s) && (getFarbe(2,2) == s)) return spielende(spieler,s);
			if ((getFarbe(1,0) == s) && (getFarbe(2,0) == s)) return spielende(spieler,s);
		}
		
		s = getFarbe(1,0);
		if ((s != Farbe.Leer) && (getFarbe(1, 1) == s) && (getFarbe(1, 2) == s)) return spielende(spieler,s);
		
		s = getFarbe(2,0);
		if (s != Farbe.Leer) {
			if ((getFarbe(2, 1) == s) && (getFarbe(2, 2) == s)) return spielende(spieler,s);;
			if ((getFarbe(1, 1) == s) && (getFarbe(0, 2) == s)) return spielende(spieler,s);;
		}
		
		s = getFarbe(0, 1);
		if ((s != Farbe.Leer) && (getFarbe(1, 1) == s) && (getFarbe(2, 1) == s)) return spielende(spieler,s);;
		
		s = getFarbe(0, 2);
		if ((s != Farbe.Leer) && (getFarbe(1, 2) == s) && (getFarbe(2, 2) == s)) return spielende(spieler,s);;
		
		//Wenn alles voll - unentschieden, sonst offen
		for (int x=0;x<3;x++) {
			for (int y=0;y<3;y++) {
				if (getFarbe(x,y) == Farbe.Leer) return Spielstand.OFFEN;
			}
		}
		return Spielstand.UNENTSCHIEDEN;
	}

	private static Spielstand spielende(Farbe f1, Farbe f2) {
		if (f1 == f2) return Spielstand.GEWONNEN;
		return Spielstand.VERLOREN;
	}


}
