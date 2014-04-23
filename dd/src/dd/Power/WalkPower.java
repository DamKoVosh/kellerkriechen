package dd.Power;

import dd.Actions.Default.Walk;
import dd.Creature.Creature;

public class WalkPower extends Power {
	
	private static WalkPower walk = new WalkPower();
	
	public static WalkPower getWalkPower() {
		return walk;
	}
	
	
	public WalkPower() {
		super();
	}
	
	@Override
	public void execute() {
		Walk walk = new Walk(performer);
		walk.execute();
	}
	
	@Override
	public String getDescription() {
		return "Walk";
	}

	@Override
	public ActionType getType() {
		return ActionType.Move;
	}
	
}
