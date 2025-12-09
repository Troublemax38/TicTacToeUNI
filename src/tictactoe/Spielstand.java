package tictactoe;

/** Spielstand:
 * OFFEN, GEWONNEN, UNENTSCHIEDEN, VERLOREN
 */
public enum Spielstand {
	
	OFFEN, GEWONNEN, UNENTSCHIEDEN, VERLOREN;
	
	/**
	 * (1) Gewonnen (-1) Verloren (0) Sonst
	 */
	public int toInt() {
		switch(this) {
		case GEWONNEN: return 1;
		case VERLOREN: return -1;
		default: return 0;
		}
	}


}
