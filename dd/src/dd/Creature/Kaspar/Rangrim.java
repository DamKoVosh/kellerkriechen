package dd.Creature.Kaspar;

import java.awt.Toolkit;
import java.util.List;

import dd.Actions.Attack.Attack;
import dd.Actions.Attack.MeleeAttack;
import dd.Actions.Attack.MeleeBasicAttack;
import dd.Actions.Default.Action;
import dd.Creature.Creature;
import dd.Encounter.Roll;
import dd.Engine.CreatureEvents;
import dd.Engine.CreatureEbents.Listener.EventHitListener;
import dd.Power.AttackPower;
import dd.Power.Power;

/**
 * Level 1 Fighter Dwarf Battlerager
 * 
 * @author kaspar
 * 
 */
public class Rangrim extends Creature {
	
	private static Power BasicAttack = new AttackPower() {
		
		@Override
		public Attack getAttack(Creature performer) {
			MeleeBasicAttack at = new MeleeBasicAttack(performer) {
				
				@Override
				protected int getAttackBase() {
					return 6;
				}
				
				@Override
				protected void rollDamage() {
					damage = Roll.r("1d8+2+6");
				}
				
				@Override
				protected void applyHitEffectsOnCreature(Creature t) {
					super.applyHitEffectsOnCreature(t);
					
					performer.giveTemporaryHitpoints(4);
					
				};
			};
			
			return at;
		}
		
		@Override
		public String getDescription() {
			return "Basic Attack";
		}
		
		@Override
		public ActionType getType() {
			return ActionType.Standard;
		}
		
	};
	
	private static Power CrushingSurge = new AttackPower() {
		
		@Override
		public String getDescription() {
			return "Crushing Surge";
		}
		
		@Override
		public ActionType getType() {
			return ActionType.Standard;
		}
		
		@Override
		public Attack getAttack(Creature performer) {
			MeleeAttack at = new MeleeAttack(performer) {
				
				@Override
				protected void rollDamage() {
					int weap;
					while ((weap = Roll.d(10)) < 3)
						;
					
					damage = weap + 6;
				}
				
				@Override
				public String getDescription() {
					return "Crushing Surge Attack";
				}
				
				@Override
				protected int getAttackBase() {
					return 6;
				}
				
				@Override
				protected void applyHitEffectsOnCreature(Creature t) {
					super.applyHitEffectsOnCreature(t);
					
					performer.giveTemporaryHitpoints(8);
					
				};
				
				@Override
				protected void applyMissEffectsOnCreature(Creature t) {
					super.applyMissEffectsOnCreature(t);
					performer.giveTemporaryHitpoints(4);
				};
				
			};
			return at;
		}
	};
	
	public Rangrim() {
		super("Rangrim", Toolkit.getDefaultToolkit().getImage(
				"Images\\Rimas.png"));
		
		this.faction = "Hero";
		
		this.ac = 18;
		this.fort = 16;
		this.ref = 12;
		this.will = 11;
		
		this.maxHp = 33;
		this.hp = maxHp;
		
		this.ini = 0;
		
		this.powers.add(CrushingSurge);
		this.powers.add(BasicAttack);
		
		
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
}
