package tictactoe;

public class Zug {
	
	private int zeile,spalte;
	
	/**
	 * Ein Zug besteht aus der Angabe einer Zeile 
	 * und einer Spalte.
	 * Die oberste Zeile hat die Nummer 0.
	 * Die linke Spalte hat die Nummer 0.
	 * @param zeile Zeile des Zugs
	 * @param spalte Spalte des Zugs
	 */
	public Zug(int zeile, int spalte) {
		assert((zeile>=0) && (zeile<3));
		assert((spalte>=0) && (spalte<3));
		this.zeile = zeile;
		this.spalte = spalte;
	}

	public int getZeile() {
		return zeile;
	}

	public int getSpalte() {
		return spalte;
	}

}
