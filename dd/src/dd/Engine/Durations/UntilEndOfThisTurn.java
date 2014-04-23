package dd.Engine.Durations;

import dd.Creature.Creature;

public class UntilEndOfThisTurn implements Duration {
	public Creature turner;
	
	public UntilEndOfThisTurn(Creature c) {
		this.turner = c;
	}
	
}
