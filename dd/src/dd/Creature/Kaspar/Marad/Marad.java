package dd.Creature.Kaspar.Marad;

import java.awt.Toolkit;
import java.util.List;

import dd.Actions.Attack.Attack;
import dd.Actions.Attack.MeleeAttack;
import dd.Actions.Default.Action;
import dd.Creature.Creature;
import dd.Encounter.Encounter;
import dd.Encounter.Roll;
import dd.Encounter.StatusEffects.StatusEffect;
import dd.Engine.CreatureEffects;
import dd.Engine.CreatureEvents;
import dd.Engine.Engine;
import dd.Engine.ModifierCollection;
import dd.Engine.CreatureEbents.Listener.GetAttackModifiersListener;
import dd.Engine.CreatureEbents.Listener.GetDamageModifiersListener;
import dd.Engine.CreatureEbents.Listener.GetPerformableActionsListener;
import dd.Engine.Durations.Effect;
import dd.Engine.Durations.UntilEndOfNextTurn;
import dd.Power.Power;
import dd.Power.Power.ActionType;

/**
 * Level 1 Hexblade Dragonborn Infernal Pact
 * 
 * @author kaspar
 * 
 */
public class Marad extends Creature {
	
	private final int BA = 8;
	
	static {
		
		CreatureEvents
				.addGetPerformableActionsListener(new GetPerformableActionsListener() {
					
					@Override
					public void getPerformableActions(Creature c,
							List<Action> actions) {
						if (c instanceof Marad) {
							Marad r = (Marad) c;
							
							actions.add(r.vengeanceStrike);
							actions.add(r.heedlessFury);
							actions.add(r.bloodOfTheMighty);
							actions.add(r.divineStrength);
							
						}
					}
				});
		
		CreatureEvents
				.addGetAttackModifiersListener(new GetAttackModifiersListener() {
					
					@Override
					public void getAttackModifiers(Creature c, Attack a,
							ModifierCollection mod) {
						
						HeedlessFuryDebuff buff;
						buff = (HeedlessFuryDebuff) CreatureEffects
								.getStatusEffectOnCreature(c,
										HeedlessFuryDebuff.class);
						if (buff == null) {
							return;
						}
						
						mod.add("Heedless Fury", 4);
					}
					
				});
		
		CreatureEvents
				.addGetDamageModifiersListener(new GetDamageModifiersListener() {
					
					@Override
					public void getDamageModifiers(Creature target,
							Attack attack, ModifierCollection mod) {
						
						DivineStrengthBuff buff;
						buff = (DivineStrengthBuff) CreatureEffects
								.getStatusEffectOnCreature(
										attack.getPerformer(),
										DivineStrengthBuff.class);
						if (buff == null) {
							return;
						}
						
						mod.add("Heedless Fury", 4);
						
					}
				});
	}
	
	private Action divineStrength = new Action(this) {
		
		@Override
		public ActionType getType() {
			return ActionType.Minor;
		}
		
		@Override
		public String getDescription() {
			return "Divine Strength";
		}
		
		@Override
		protected void execute() {
			CreatureEffects
			.addStatusEffect(new Effect(performer,
					new DivineStrengthBuff(), new UntilEndOfNextTurn(
							performer)));
		}
	};
	
	private MeleeAttack vengeanceStrike = new MeleeAttack(this) {
		
		@Override
		protected void rollDamage() {
			
			int adjacentEnemyCount = 0;
			
			for (Creature c : Encounter.getCurrentEncounter().combatants) {
				if (c == performer)
					continue;
				
				if (Engine.getCreatureDistance(c, performer) == 1) {
					if (c.isEnemyOf(performer)) {
						adjacentEnemyCount++;
					}
				}
			}
			
			int bonusDamage = adjacentEnemyCount * 2;
			
			damage = Roll.r("1d12 + 5 + " + bonusDamage);
		}
		
		@Override
		public String getDescription() {
			return "Vengeance Strike";
		}
		
		@Override
		protected int getAttackBase() {
			return BA;
		}
	};
	
	private MeleeAttack heedlessFury = new MeleeAttack(this) {
		
		@Override
		public String getDescription() {
			return "Heedless Fury";
		}
		
		@Override
		protected int getAttackBase() {
			return BA;
		}
		
		@Override
		protected void rollDamage() {
			damage = Roll.r("3d12 + 5");
		}
		
		@Override
		protected void execute() {
			super.execute();
			
			CreatureEffects
					.addStatusEffect(new Effect(performer,
							new HeedlessFuryDebuff(), new UntilEndOfNextTurn(
									performer)));
			
		}
		
	};
	
	private MeleeAttack bloodOfTheMighty = new MeleeAttack(this) {
		@Override
		public String getDescription() {
			return "Blood Of The Mighty";
		}
		
		@Override
		protected int getAttackBase() {
			return BA;
		}
		
		@Override
		protected void rollDamage() {
			damage = Roll.r("4d12 + 5");
		}
		
		@Override
		protected void execute() {
			super.execute();
			
			performer.takeDamage(5, DamageType.Untyped, performer,
					"Blood Of The Mighty");
		}
	};
	
	private class HeedlessFuryDebuff extends StatusEffect {
		
		@Override
		public String getDescription() {
			return "Heedless Fury Debuff -5 to all defenses";
		}
		
	}
	
	private class DivineStrengthBuff extends StatusEffect {
		
		@Override
		public String getDescription() {
			return "Divine Might +5 to damage roll of next attack";
		}
		
	}
	
	public Marad() {
		super("Marad", Toolkit.getDefaultToolkit()
				.getImage("Images\\Marad.png"));
		
		this.faction = "Hero";
		
		this.ac = 18;
		this.fort = 16;
		this.ref = 12;
		this.will = 13;
		
		this.maxHp = 35;
		this.hp = maxHp;
		
		this.ini = 1;
		
	}
	
	@Override
	public int getDefaultSpeed() {
		return 5;
	}
	
	@Override
	public List<Action> getOpportunityActions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Power> getPowers() {
		// TODO Auto-generated method stub
		return null;
	}
}
