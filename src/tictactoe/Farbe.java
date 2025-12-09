package tictactoe;

/** Farbe: Kreuz, Kreis oder Leer
 * 
 */
public enum Farbe {
	
	Kreuz, Kreis, Leer;
	
	/** Gibt O, X oder _ aus
	 * 
	 */
	public String toString() {
		switch(this) {
		case Kreis: return "O";
		case Kreuz: return "X";
		default: return "_";
		}
	}
	
	/**
	 * 1 für Kreis, 2 für Kreuz, 0 wenn leer
	 */
	public int toInt() {
		switch(this) {
		case Kreis: return 1;
		case Kreuz: return 2;
		default: return 0;
		}
	}
	
	/**
	 * 1 für Kreis, -1 für Kreuz, 0 wenn leer
	 */
	public int toInput() {
		switch(this) {
		case Kreis: return 1;
		case Kreuz: return -1;
		default: return 0;
		}
	}
	
	/**
	 * Gegenteil einer Farbe: Kreuz ==> Kreis
	 * @return Kreis nach Kreuz und umgekehrt.
	 */
	public Farbe opposite() {
		switch(this) {
		case Kreis: return Kreuz;
		case Kreuz: return Kreis;
		default: return Leer;
	}
	}
}
