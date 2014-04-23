package dd.Engine.Durations;

import dd.Creature.Creature;

public class UntilEndOfNextTurn implements Duration {
	
	public Creature turner;
	public boolean next;
	
	public UntilEndOfNextTurn(Creature c) {
		this.turner = c;
		next = false;
	}
	
}
