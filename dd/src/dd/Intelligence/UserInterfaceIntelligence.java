package dd.Intelligence;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import dd.Actions.Attack.Attack;
import dd.Actions.Default.Action;
import dd.Actions.Default.Walk;
import dd.Creature.Creature;
import dd.Encounter.Encounter;
import dd.Engine.Engine;
import dd.Movement.Move;
import dd.Movement.PathFinding;
import dd.Power.AttackPower;
import dd.Power.Power;
import dd.Power.Power.ActionType;
import dd.Power.WalkPower;
import dd.UI.MapPanel;
import dd.UI.Window;

public class UserInterfaceIntelligence implements Intelligence {
	
	private static UserInterfaceIntelligence currentIntelligence = null;
	
	private Creature creature = null;
	private Move move = null;
	
	private Window w = Engine.getWindow();
	
	private AttackPower selectedAttack;
	
	private static Point clickedStep;
	private static Power nextAction = null;
	
	private static List<Point> autoMovePath;
	
	public static boolean autoMove = false;
	
	public static UserInterfaceIntelligence getCurrentUii() {
		return currentIntelligence;
	}
	
	private List<Creature> preparedTargets = new LinkedList<Creature>();
	
	private String lastAttack;
	
	private boolean endTurn;
	
	private boolean duringAction = false;
	private Power currentAction;
	
	@Override
	public void takeActions(Creature c) {
		
		currentIntelligence = this;
		creature = c;
		
		endTurn = false;
		
		showActions(c);
		
		while (!endTurn) {
			
			if (w.endTurnClicked) {
				w.endTurnClicked = false;
				endTurn = true;
			}
			
			idle();
			
			if (nextAction != null) {
				
				
				currentAction = nextAction;
				nextAction = null;
				
				w.getMap().showPath(null, 0);
				
				duringAction = true;
				currentAction.perform(c);
				duringAction = false;
				
				showActions(c);
				postPerformAction(nextAction);
			}
			
		}
		w.getMap().showPath(null, 0);
	}
	
	private void postPerformAction(Power nextAction2) {
		selectedAttack = null;
	}
	
	private void idle() {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void takeAction(String action) {
		for (Power a : creature.getPowers()) {
			if (a.getDescription() == action) {
				nextAction = a;
				return;
			}
		}
		
		Engine.log("Warning: Action \"%s\" not found", action);
		
	}
	
	private void showActions(Creature c) {
		w.clearActionLists();
		
		String type;
		for (Action a : c.avaiableActionList) {
			
			if (a.getType() == ActionType.Standard) {
				type = "Standard";
			} else if (a.getType() == ActionType.Move) {
				type = "Move";
			} else if (a.getType() == ActionType.Minor) {
				type = "Minor";
			} else {
				type = "Free";
			}
			
			w.addAction(type, a.getDescription());
		}
		
	}
	
	@Override
	public Point getNextMoveStep(Move move, Creature c) {
		this.move = move;
		clickedStep = null;
		
		// w.getMap().showMoveSteps(c);
		
		while (clickedStep == null) {
			if (autoMove) {
				if (autoMovePath.size() > 0) {
					clickedStep = autoMovePath.get(0);
					autoMovePath.remove(0);
					
					idle();
					break;
					
				} else {
					autoMove = false;
					currentIntelligence.w.getMap().showPath(null,
							currentIntelligence.move.remainingSteps + 1);
					
					currentIntelligence.w.updateMap();
				}
				
			}
			
			idle();
			
			// Cancel/finish move if new action is ordered
			if (nextAction != null || endTurn) {
				Engine.log("Got new action. Canceling move.");
				clickedStep = null;
				break;
			}
		}
		
		w.getMap().showPath(null, 0);
		// w.getMap().showMoveSteps(null);
		
		this.move = null;
		
		return clickedStep;
	}
	
	public void autoMove(List<Point> path) {
		
		if (autoMove == false || move == null) {
			autoMovePath = path;
			autoMove = true;
			
			if (move == null) {
				Power walk = getAvaiableWalkAction();
				
				if (walk != null)
					nextAction = walk;
				else
					autoMove = false;
			}
			
		}
	}
	
	private WalkPower getAvaiableWalkAction() {
		List<Power> list = creature.getPowers();
		WalkPower walk = null;
		
		for (Power a : list) {
			if (a instanceof WalkPower) {
				walk = (WalkPower) a;
				break;
			}
		}
		return walk;
	}
	
	public static void clickStep(Point p) {
		int dist;
		dist = Engine
				.getDistance(currentIntelligence.creature.getPosition(), p);
		
		if (autoMovePath != null && autoMovePath.size() > 0) {
			if (p.equals(autoMovePath.get(autoMovePath.size() - 1))) {
				// clickedStep = autoMovePath.get(1);
				// autoMovePath.remove(0);
				autoMove = true;
			}
		}
		
		if (dist > 1) {
			
			autoMovePath = PathFinding.findShortestPath(
					currentIntelligence.creature.getPosition(), p);
			
			currentIntelligence.w.getMap().showPath(autoMovePath,
					currentIntelligence.move.remainingSteps);
			
			currentIntelligence.w.updateMap();
			
		} else {
			clickedStep = p;
		}
	}
	
	public void clickMove(Point p, MapPanel map) {
		
		List<Point> path = PathFinding.findShortestPath(
				currentIntelligence.creature.getPosition(), p);
		
		map.showPath(path, getMoveDistanceLeft());
		
	}
	
	private int getMoveDistanceLeft() {
		if (this.move != null) {
			return this.move.remainingSteps;
		} else {
			WalkPower walk = getAvaiableWalkAction();
			
			if (walk != null) {
				return creature.getSpeed();
			} else {
				return 0;
			}
		}
		
	}
	
	public static void useActionButton(String label, String action) {
		if (currentIntelligence == null) {
			return;
		}
		
		currentIntelligence.takeAction(action);
	}
	
	@Override
	public List<Creature> selectTargets(Attack attack, int maxTargets) {
		
		if (preparedTargets.size() > 0) {
			List<Creature> list;
			list = new ArrayList<Creature>(preparedTargets);
			preparedTargets.clear();
			
			return list;
		} else {
			
			List<Creature> targets = new LinkedList<Creature>();
			
			for (Creature c : Encounter.getCurrentEncounter().combatants) {
				if (targets.size() > maxTargets)
					break;
				
				if (!c.isEnemyOf(creature))
					continue;
				
				if (attack.isValidTarget(c)) {
					targets.add(c);
				}
			}
			
			return targets;
			
		}
		
	}
	
	public AttackPower getUiSelectedAttack() {
		if (selectedAttack == null) {
			for (Power c : creature.getPowers()) {
				if (c.getDescription().equals(lastAttack)) {
					if (c instanceof AttackPower) {
						selectedAttack = (AttackPower) c;
					}
				}
			}
		}
		
		return selectedAttack;
	}
	
	public void uiSelectNextAttack() {
		List<Power> list = creature.getPowers();
		
		List<AttackPower> attackList = new LinkedList<AttackPower>();
		
		for (Power a : list) {
			if (a instanceof AttackPower) {
				attackList.add((AttackPower) a);
			}
		}
		
		AttackPower attack = null;
		boolean useNext = false;
		
		for (Iterator<AttackPower> it = attackList.iterator(); it.hasNext();) {
			
			attack = it.next();
			if (useNext)
				break;
			
			if (attack == selectedAttack) {
				useNext = true;
				if (!it.hasNext()) {
					it = attackList.iterator();
				}
			}
		}
		
		selectedAttack = attack;
		
	}
	
	public void uiPrepareTarget(Creature c) {
		preparedTargets.add(c);
	}
	
	public void uiPerformAttack(AttackPower a) {
		lastAttack = a.getDescription();
		nextAction = a;
	}
	
	public void uiEndTurn() {
		endTurn = true;
	}
	
	@Override
	public boolean takeReaction() {
		return true;
	}
}
