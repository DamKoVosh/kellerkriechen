package dd.Creature;

import java.awt.Graphics;
import java.awt.Image;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import dd.Actions.Attack.Attack;
import dd.Actions.Default.Action;
import dd.Encounter.Encounter;
import dd.Engine.CreatureEvents;
import dd.Engine.Engine;
import dd.Engine.ModifierCollection;
import dd.Intelligence.Intelligence;
import dd.Intelligence.UserInterfaceIntelligence;
import dd.MapObjects.MapObject;
import dd.Power.Power;
import dd.Power.Power.ActionType;
import dd.Power.WalkPower;

public abstract class Creature extends MapObject {
	
	Image img;
	private String name;
	private Encounter currentEncounter;
	
	public ActionSet takenActions;
	public ActionSet avaiableActions;
	
	public String faction;
	
	public Intelligence intelligence;
	
	public List<Action> avaiableActionList = new LinkedList<Action>();
	
	protected int hp;
	protected int maxHp;
	protected int ac, fort, ref, will;
	protected int ini;
	private int thp;
	
	protected List<Power> powers = new LinkedList<Power>();
	
	public enum Defense {
		AC("AC"), Fort("Fortitude"), Ref("Reflex"), Will("Will");
		
		private String description;
		
		private Defense(String description) {
			this.description = description;
		}
		
		public String getDescription() {
			return description;
		}
	}
	
	public int getHp() {
		return hp;
	}
	
	public void setHp(int newHp) {
		hp = newHp;
	}
	
	public int getMaxHp() {
		return maxHp;
	}
	
	public int getDefense(Defense d) {
		switch (d) {
		case AC:
			return ac;
		case Fort:
			return fort;
		case Ref:
			return ref;
		case Will:
			return will;
		}
		return 0;
	}
	
	public Creature(String name, Image img) {
		super(0, 0);
		
		this.name = name;
		this.img = img;
		
		this.intelligence = new UserInterfaceIntelligence();
		
		this.powers.add(WalkPower.getWalkPower());
		
	}
	
	public int getInitiative() {
		return ini;
	}
	
	public boolean canPerformAction(Action action) {
		if (action.getType() == ActionType.Standard) {
			if (avaiableActions.standard > 0) {
				return true;
			}
		} else if (action.getType() == ActionType.Move) {
			if (avaiableActions.move > 0) {
				return true;
			}
		} else if (action.getType() == ActionType.Minor) {
			if (avaiableActions.minor > 0) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean canPerformPower(Power power) {
		if (power.getType() == ActionType.Standard) {
			if (avaiableActions.standard > 0) {
				return true;
			}
		} else if (power.getType() == ActionType.Move) {
			if (avaiableActions.move > 0) {
				return true;
			}
		} else if (power.getType() == ActionType.Minor) {
			if (avaiableActions.minor > 0) {
				return true;
			}
		}
		
		return false;
	}
	
	public void takeTurn() {
		
		startTurn();
		
		takeActions();
		
		endTurn();
	}
	
	private void endTurn() {
		CreatureEvents.eventEndTurn(this);
	}
	
	private void startTurn() {
		takenActions = new ActionSet(0, 0, 0);
		avaiableActions = new ActionSet(1, 1, 1);
		
		CreatureEvents.eventGiveActions(this);
		
		CreatureEvents.eventStartTurn(this);
	}
	
	private void takeActions() {
		
		updatePerformableActions();
		
		intelligence.takeActions(this);
	}
	
	@Deprecated
	public void updatePerformableActions() {
		avaiableActionList = getPerformableActions();
	}
	
	public String getName() {
		return name;
	}
	
	public void takePartInEncounter(Encounter encounter) {
		this.currentEncounter = encounter;
	}
	
	public boolean isTakingTurn() {
		if (currentEncounter == null) {
			return false;
		} else {
			if (currentEncounter.getCurrentTurnActor() == this) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public void takeShortRest() {
		
	}
	
	public void takeExtendedRest() {
		
	}
	
	public List<Action> getPerformableActions() {
		List<Action> list = new LinkedList<Action>();
		
		CreatureEvents.eventGetPerformableActions(this, list);
		
		Iterator<Action> it = list.iterator();
		Action a;
		while (it.hasNext()) {
			a = it.next();
			if (!canPerformAction(a)) {
				it.remove();
			}
		}
		
		return list;
	}
	
	public boolean canDoAction(Action action) {
		return true;
	}
	
	public int getSpeed() {
		return getDefaultSpeed();
	}
	
	protected int getDefaultSpeed() {
		return 6;
	}
	
	public boolean isAllyOf(Creature c) {
		return !isEnemyOf(c) && !(c == this);
	}
	
	public boolean isEnemyOf(Creature c) {
		if (c.faction != this.faction) {
			return true;
		} else {
			return false;
		}
	}
	
	public enum DamageType {
		Untyped, Fire
	}
	
	public void takeDamage(int damage, DamageType type, Creature damager,
			String description) {
		Engine.log("%s deals %d damage to %s (%s)", damager.getName(), damage,
				this.getName(), description);
		
		CreatureEvents.eventTakeDamage(this, damage, type, damager);
		
		int thpDamage;
		thpDamage = Math.min(thp, damage);
		this.thp -= thpDamage;
		damage = damage - thpDamage;
		
		this.hp -= damage;
		
		Engine.log("%s has %d hp left", this.getName(), this.hp);
		
		if (this.thp > 0) {
			Engine.log("%s has %d temporary hp left", this.getName(), this.thp);
		}
		
		if (this.hp <= 0) {
			die(damager);
		}
	}
	
	public void die(Creature killer) {
		Engine.log("%s dies", this.getName());
		Engine.removeCreature(this);
		
		CreatureEvents.eventDie(this, killer);
	}
	
	public int getDefenseModifiers(Attack attack) {
		return 0;
	}
	
	public Integer getAttackModifiers(Attack attack, Creature target) {
		
		ModifierCollection mod = new ModifierCollection();
		
		CreatureEvents.eventGetAttackModifiers(target, attack, mod);
		
		return mod.getTotalValue();
	}
	
	public int getDamageModifiers(Attack attack, Creature target) {
		ModifierCollection mod = new ModifierCollection();
		
		CreatureEvents.eventGetDamageModifiers(target, attack, mod);
		
		return mod.getTotalValue();
	}
	
	public List<Creature> getEnemies() {
		List<Creature> enemies = new LinkedList<Creature>();
		
		for (Creature c : Encounter.getCurrentEncounter().combatants) {
			if (this.isEnemyOf(c)) {
				enemies.add(c);
			}
		}
		
		return enemies;
	}
	
	public void giveTemporaryHitpoints(int amount) {
		if (this.thp < amount) {
			this.thp = amount;
		}
		
		Engine.log("%s gains %d temporary hp", this.getName(), this.thp);
		
		CreatureEvents.eventGainThp(this, amount);
	}
	
	public void setName(String string) {
		this.name = string;
	}
	
	public abstract List<Action> getOpportunityActions();
	
	public List<Power> getPowers() {
		return powers;
	}
	
	public List<Power> getPerformablePowers() {
		List<Power> list = new LinkedList<Power>(powers);
		
		Power p;
		for (Iterator<Power> it = list.iterator(); it.hasNext();) {
			p = it.next();
			if (!canPerformPower(p)) {
				it.remove();
			}
		}
		
		return list;
	}

	
	
}
