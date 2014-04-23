package dd.Actions.Attack;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dd.Creature.Creature;
import dd.Creature.Creature.DamageType;
import dd.Creature.Creature.Defense;
import dd.Encounter.Roll;
import dd.Engine.CreatureEvents;
import dd.Engine.Engine;

public abstract class MeleeAttack extends Attack {
	
	public MeleeAttack(Creature performer) {
		super(performer);
		
		maxTargets = 1;
		range = 1;
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
		return "Melee Attack";
	}
	
}
