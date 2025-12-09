package tictactoe.spieler;

import java.io.IOException;

/** Schnittstelle für alle lernenden Spieler */
public interface ILernenderSpieler extends ISpieler {
	
	/** Training für einen lernenden Spieler. Die Abbruchbedingung regelt, wie lange
	 * gelernt wird. Wenn das Training erfolgreich war, wird true zurückgeliefert.
	 * @param abbruchbedingung
	 * @return
	 */
	boolean trainieren(IAbbruchbedingung abbruchbedingung);
	
	void speichereWissen(String name) throws IOException;
	
	void ladeWissen(String name) throws IOException;

}
