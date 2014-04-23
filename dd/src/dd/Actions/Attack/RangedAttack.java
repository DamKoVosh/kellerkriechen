package dd.Actions.Attack;

import dd.Creature.Creature;
import dd.Creature.Creature.DamageType;
import dd.Creature.Creature.Defense;
import dd.Engine.Engine;

public abstract class RangedAttack extends Attack {
	
	public RangedAttack(Creature performer) {
		super(performer);
		
		maxTargets = 1;
		range = 10;
		def = Defense.AC;
		damageType = DamageType.Untyped;
	}
	
	@Override
	public boolean isValidTarget(Creature c) {
		if (Engine.getCreatureDistance(performer, c) > range) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public String getDescription() {
		return "Ranged Attack";
	}
	
}
