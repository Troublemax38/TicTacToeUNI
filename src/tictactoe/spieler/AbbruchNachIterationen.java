package tictactoe.spieler;

public class AbbruchNachIterationen implements IAbbruchbedingung {
	private int maxIter;
	private int aktIter;
	
    public AbbruchNachIterationen(int anzahlIterationen) {
	   this.maxIter = anzahlIterationen;
	}
	
	@Override
	public boolean abbruch() {
		return ++aktIter >= maxIter;
	}
}
