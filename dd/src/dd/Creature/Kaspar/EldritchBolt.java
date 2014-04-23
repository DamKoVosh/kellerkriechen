package dd.Creature.Kaspar;

import dd.Actions.Attack.RangedAttack;
import dd.Creature.Creature;
import dd.Creature.Creature.Defense;
import dd.Encounter.Roll;

public class EldritchBolt extends RangedAttack {
	
	public EldritchBolt(Creature performer) {
		super(performer);
		
		this.def = Defense.Ref;
		this.range = 10;
	}
	
	@Override
	public String getDescription() {
		return "Eldritch Bolt";
	}

	@Override
	protected int getAttackBase() {
		return 5;
	}

	@Override
	protected void rollDamage() {
		damage = Roll.r("1d10 + 9");
	}
	
}
