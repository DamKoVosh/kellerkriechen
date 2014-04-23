package dd.Engine.Durations;

import dd.Creature.Creature;

public class UntilStartOfNextTurn implements Duration {
	
	public Creature turner;
	
	public UntilStartOfNextTurn(Creature c) {
		this.turner = c;
	}
	
}
