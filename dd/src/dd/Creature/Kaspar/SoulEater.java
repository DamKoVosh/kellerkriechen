package dd.Creature.Kaspar;

import dd.Actions.Attack.Attack;
import dd.Actions.Attack.MeleeAttack;
import dd.Creature.Creature;
import dd.Encounter.Roll;
import dd.Encounter.StatusEffects.StatusEffect;
import dd.Engine.ModifierCollection;
import dd.Engine.CreatureEffects;
import dd.Engine.CreatureEvents;
import dd.Engine.CreatureEbents.Listener.GetAttackModifiersListener;
import dd.Engine.Durations.Effect;
import dd.Engine.Durations.UntilEndOfNextTurn;

public class SoulEater extends MeleeAttack {
	
	static {
		CreatureEvents
				.addGetAttackModifiersListener(new GetAttackModifiersListener() {
					
					@Override
					public void getAttackModifiers(Creature c, Attack a,
							ModifierCollection mod) {
						
						SoulEaterBuff buff;
						buff = (SoulEaterBuff) CreatureEffects
								.getStatusEffectOnCreature(a.getPerformer(),
										SoulEaterBuff.class);
						if (buff == null) {
							return;
						}
						
						if (buff.getTarget() == c) {
							mod.add("power", 2);
						}
						
					}
					
				});
	}
	
	private class SoulEaterBuff extends StatusEffect {
		
		private Creature target;
		
		private SoulEaterBuff(Creature target) {
			this.target = target;
		}
		
		private Creature getTarget() {
			return target;
		}
		
		@Override
		public String getDescription() {
			return "Soul Eater Buff +2 AT against certain enemy";
		}
		
	}
	
	
	public SoulEater(Creature performer) {
		super(performer);
	}
	
	@Override
	protected int getAttackBase() {
		return 7;
	}
	
	@Override
	protected void rollDamage() {
		// damage = Roll.d(12) + 9;
		damage = Roll.r("1d12 + 9");
	}
	
	@Override
	public String getDescription() {
		return "Soul Eater"; //  (Blade of Annihilation)
	}
	
	@Override
	protected void applyHitEffectsOnCreature(Creature target) {
		super.applyHitEffectsOnCreature(target);
		
		CreatureEffects.addStatusEffect(new Effect(performer,
				new SoulEaterBuff(target), new UntilEndOfNextTurn(performer)));
	}
	
}
