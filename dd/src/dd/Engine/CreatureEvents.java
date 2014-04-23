package dd.Engine;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import dd.Actions.Attack.Attack;
import dd.Actions.Default.Action;
import dd.Creature.ActionSet;
import dd.Creature.Creature;
import dd.Creature.Creature.DamageType;
import dd.Encounter.StatusEffects.StatusEffect;
import dd.Engine.CreatureEbents.Listener.DieListener;
import dd.Engine.CreatureEbents.Listener.EndTurnListener;
import dd.Engine.CreatureEbents.Listener.EnterSquareListener;
import dd.Engine.CreatureEbents.Listener.GainThpListener;
import dd.Engine.CreatureEbents.Listener.GetAttackModifiersListener;
import dd.Engine.CreatureEbents.Listener.GetDamageModifiersListener;
import dd.Engine.CreatureEbents.Listener.GetPerformableActionsListener;
import dd.Engine.CreatureEbents.Listener.GiveActionListener;
import dd.Engine.CreatureEbents.Listener.HitListener;
import dd.Engine.CreatureEbents.Listener.LeaveSquareListener;
import dd.Engine.CreatureEbents.Listener.StartTurnListener;
import dd.Movement.Move;

public class CreatureEvents {
	private static List<GiveActionListener> giveActionListenerList = new LinkedList<GiveActionListener>();
	private static List<StartTurnListener> startTurnListenerList = new LinkedList<StartTurnListener>();
	private static List<EndTurnListener> endTurnListenerList = new LinkedList<EndTurnListener>();
	private static List<GetPerformableActionsListener> getPerformableActionsListenerList = new LinkedList<GetPerformableActionsListener>();
	private static List<GetAttackModifiersListener> getGetAttackModifiersListenerList = new LinkedList<GetAttackModifiersListener>();
	private static List<GetDamageModifiersListener> getGetDamageModifiersListenerList = new LinkedList<GetDamageModifiersListener>();
	private static List<DieListener> dieListenerList = new LinkedList<DieListener>();
	private static List<HitListener> hitListenerList = new LinkedList<HitListener>();
	private static List<MissListener> missListenerList = new LinkedList<MissListener>();
	private static List<TakeDamageListener> takeDamageListenerList = new LinkedList<TakeDamageListener>();
	private static List<GainThpListener> gainThpListenerList = new LinkedList<GainThpListener>();
	private static List<LeaveSquareListener> leaveSquareListenerList = new LinkedList<LeaveSquareListener>();
	private static List<EnterSquareListener> enterSquareListenerList = new LinkedList<EnterSquareListener>();
	
	private static List<CreatureEventListener> eventListenerList = new LinkedList<CreatureEventListener>();
	
	public static void addCreatureEventListener(CreatureEventListener l) {
		
		eventListenerList.add(l);
	}
	
	public static void eventCreature(String eventName, Object... args) {
		for (CreatureEventListener l : eventListenerList) {
			
			if (!l.getEventName().equals(eventName))
				continue;
			
			
			
			l.onEvent(args);
			
		}
	}
	
	public static void addEnterSquareListener(EnterSquareListener l) {
		enterSquareListenerList.add(l);
	}
	
	public static void addLeaveSquareListener(LeaveSquareListener l) {
		leaveSquareListenerList.add(l);
	}
	
	public static void addGainThpListener(GainThpListener l) {
		gainThpListenerList.add(l);
	}
	
	public static void addTakeDamageListener(TakeDamageListener l) {
		takeDamageListenerList.add(l);
	}
	
	public static void addHitListener(HitListener l) {
		hitListenerList.add(l);
	}
	
	public static void addMissListener(MissListener l) {
		missListenerList.add(l);
	}
	
	public static void addGetPerformableActionsListener(
			GetPerformableActionsListener l) {
		
		getPerformableActionsListenerList.add(l);
		
	}
	
	public static void removeGetPerformableActionsListener(StatusEffect key) {
		getPerformableActionsListenerList.remove(key);
	}
	
	public static void addGiveActionListener(
			GiveActionListener giveActionListener) {
		
		giveActionListenerList.add(giveActionListener);
		
	}
	
	public static void removeGiveActionListener(StatusEffect key) {
		giveActionListenerList.remove(key);
	}
	
	public static void addStartTurnListener(StartTurnListener l) {
		startTurnListenerList.add(l);
	}
	
	public static void addGetAttackModifiersListener(
			GetAttackModifiersListener l) {
		getGetAttackModifiersListenerList.add(l);
	}
	
	public static void addGetDamageModifiersListener(
			GetDamageModifiersListener l) {
		getGetDamageModifiersListenerList.add(l);
	}
	
	public static void addDieListener(DieListener l) {
		dieListenerList.add(l);
	}
	
	public static void eventStartTurn(Creature c) {
		for (StartTurnListener l : startTurnListenerList) {
			l.startTurn(c);
		}
	}
	
	public static void addEndTurnListener(EndTurnListener l) {
		endTurnListenerList.add(l);
	}
	
	public static void eventEndTurn(Creature c) {
		for (EndTurnListener l : endTurnListenerList) {
			l.endTurn(c);
		}
	}
	
	public static void eventGiveActions(Creature c) {
		
		ActionSet actions = c.avaiableActions;
		
		for (GiveActionListener l : giveActionListenerList) {
			actions = l.calculateActions(c, actions);
		}
		
		c.avaiableActions = actions;
	}
	
	public static void eventGetPerformableActions(Creature c, List<Action> list) {
		for (GetPerformableActionsListener l : getPerformableActionsListenerList) {
			l.getPerformableActions(c, list);
		}
	}
	
	public static void eventGetAttackModifiers(Creature creature,
			dd.Actions.Attack.Attack a, ModifierCollection mod) {
		
		for (GetAttackModifiersListener l : getGetAttackModifiersListenerList) {
			// l.getPerformableActions(c, list);
			l.getAttackModifiers(creature, a, mod);
		}
		
	}
	
	public static void eventGetDamageModifiers(Creature c,
			dd.Actions.Attack.Attack a, ModifierCollection mod) {
		
		for (GetDamageModifiersListener l : getGetDamageModifiersListenerList) {
			// l.getPerformableActions(c, list);
			l.getDamageModifiers(c, a, mod);
		}
		
	}
	
	public static void eventLeaveSquare(Creature c, Point position, Move move) {
		for (LeaveSquareListener l : leaveSquareListenerList) {
			l.leaveSquare(c, position, move);
		}
	}
	
	public static void eventEnterSquare(Creature c, Point newPosition, Move move) {
		for (EnterSquareListener l : enterSquareListenerList) {
			l.enterSquare(c, newPosition, move);
		}
	}
	
	public static void eventDie(Creature creature, Creature killer) {
		
		for (DieListener l : dieListenerList) {
			l.die(creature, killer);
		}
	}
	
	public static void eventHit(Attack attack, Creature target) {
		for (HitListener l : hitListenerList) {
			l.hit(attack, target);
		}
	}
	
	public static void eventMiss(Attack attack, Creature target) {
		for (MissListener l : missListenerList) {
			l.miss(attack, target);
		}
	}
	
	public static void eventTakeDamage(Creature creature, int damage,
			DamageType type, Creature damager) {
		for (TakeDamageListener l : takeDamageListenerList) {
			l.takeDamage(creature, damage, type, damager);
		}
	}
	
	public static void eventGainThp(Creature creature, int amount) {
		for (GainThpListener l : gainThpListenerList) {
			l.gainThp(creature, amount);
		}
	}
	
}
