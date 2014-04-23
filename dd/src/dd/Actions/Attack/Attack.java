package dd.Actions.Attack;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dd.Actions.Default.Action;
import dd.Power.Power.ActionType;
import dd.Power.Power;
import dd.Creature.Creature;
import dd.Creature.Creature.DamageType;
import dd.Creature.Creature.Defense;
import dd.Encounter.Roll;
import dd.Engine.CreatureEvents;
import dd.Engine.Engine;
import dd.Engine.CreatureEbents.Listener.EventHitListener;

public abstract class Attack extends Action {
	
	protected List<Creature> targets;
	protected int maxTargets;
	protected int range;
	protected Defense def;
	protected Map<Creature, Integer> attackRollDice;
	protected List<Creature> targetsHit = new LinkedList<Creature>();
	protected List<Creature> targetsMissed = new LinkedList<Creature>();
	protected int damage;
	protected DamageType damageType;
	
	public Attack(Creature performer) {
		super(performer);
	}
	
	protected abstract int getAttackBase();
	
	@Override
	protected void execute() {
		Engine.log("%s begins attack %s", performer.getName(), getDescription());
		
		selectTargets();
		
		if (targets.isEmpty()) {
			Engine.log("Attack has no targets");
			return;
		}
		
		rollAttacks();
		
		checkHits();
		
		rollDamage();
		applyHitEffects();
		applyMissEffects();
		
	}
	
	protected void checkHits() {
		
		targetsHit.clear();
		targetsMissed.clear();
		
		int attack;
		int defense;
		for (Creature t : targets) {
			int roll, base, mod;
			roll = attackRollDice.get(t);
			base = getAttackBase();
			mod = performer.getAttackModifiers(this, t);
			
			attack = roll + base + mod;
			Engine.log("Rolled %d + %d base + %d mod", roll, base, mod);
			
			int defBase, defMod;
			defBase = t.getDefense(def);
			defMod = t.getDefenseModifiers(this);
			defense = defBase + defMod;
			// Engine.log("Defense is %d + %d mod", defBase, defMod);
			
			if ((attack >= defense && roll != 1) || roll == 20) {
				targetsHit.add(t);
				Engine.log("Attack hits %s: %d vs. %d (%s)", t.getName(),
						attack, defense, def.getDescription());
				
				CreatureEvents.eventCreature(EventHitListener.EVENT_HIT, this, t);
				
				
			} else {
				targetsMissed.add(t);
				
				Engine.log("Attack misses %s: %d vs. %d (%s)", t.getName(),
						attack, defense, def.getDescription());
			}
		}
	}
	
	protected abstract void rollDamage();
	
	protected void applyHitEffects() {
		
		for (Creature t : targetsHit) {
			applyHitEffectsOnCreature(t);
		}
	}
	
	protected void applyHitEffectsOnCreature(Creature t) {
		
		CreatureEvents.eventHit(this, t);
		
		int modifiedDamage;
		modifiedDamage = damage + t.getDamageModifiers(this, t);
		
		t.takeDamage(modifiedDamage, damageType, performer, getDescription());
		
	}
	
	protected void applyMissEffects() {
		for (Creature t : targetsMissed) {
			applyMissEffectsOnCreature(t);
		}
	}
	
	protected void applyMissEffectsOnCreature(Creature t) {
		
		CreatureEvents.eventMiss(this, t);
	}
	
	protected void rollAttack(Creature c) {
		int roll;
		
		roll = Roll.d20();
		
		attackRollDice.put(c, roll);
	}
	
	protected void rollAttacks() {
		attackRollDice = new HashMap<Creature, Integer>();
		
		for (Creature t : targets) {
			rollAttack(t);
		}
	}
	
	protected void selectTargets() {
		
		targets = new LinkedList<Creature>();
		
		for (Creature t : performer.intelligence
				.selectTargets(this, maxTargets)) {
			
			if (targets.size() >= maxTargets) {
				break;
			}
			
			if (isValidTarget(t)) {
				targets.add(t);
			}
		}
		
	}
	
	@Override
	public ActionType getType() {
		return ActionType.Standard;
	}
	
	public List<Creature> getTargetsHit() {
		return targetsHit;
	}
	
	public abstract boolean isValidTarget(Creature c);
	
	public Creature getPerformer() {
		return performer;
	}
}
