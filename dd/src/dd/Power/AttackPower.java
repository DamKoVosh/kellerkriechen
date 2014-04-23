package dd.Power;

import dd.Actions.Attack.Attack;
import dd.Creature.Creature;

public abstract class AttackPower extends Power {
	
	public abstract Attack getAttack(Creature performer);
	
	@Override
	protected void execute() {
		getAttack(performer).perform();
	}
	
}
