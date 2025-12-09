package tictactoe.spieler;

import java.util.Date;

public class AbbruchNachZeit implements IAbbruchbedingung {

	private long ende;

    public AbbruchNachZeit(int zeitInSekunden) {
	   this.ende = (new Date()).getTime() + (long)(zeitInSekunden * 1000);
	}
	
	@Override
	public boolean abbruch() {
	   return (new Date()).getTime() > this.ende;
	}

}
