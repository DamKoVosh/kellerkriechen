package dd.Actions.Default;

import java.util.LinkedList;
import java.util.List;

import dd.Creature.Creature;
import dd.Engine.Engine;
import dd.Power.Power.ActionType;

public abstract class Action {
	
	
	
	protected Creature performer;
	
	public abstract ActionType getType();
	
	private List<Action> reactions = new LinkedList<Action>();
	
	public Action(Creature performer) {
		this.performer = performer;
	}
	
	public void addReaction(Action reaction) {
		reactions.add(reaction);
	}
	
	private void performReactions() {
		for (Action a : reactions) {
			a.perform();
		}
	}
	
	public void perform() {
		if (getType() == ActionType.Standard) {
			performer.avaiableActions.standard--;
		} else if (getType() == ActionType.Move) {
			performer.avaiableActions.move--;
		} else if (getType() == ActionType.Minor) {
			performer.avaiableActions.minor--;
		}
		
		reactions.clear();
		
		execute();
		
		performReactions();
		
		performer.updatePerformableActions();
		
		Engine.getWindow().updateMap();
	}
	
	

	protected abstract void execute();
	
	public abstract String getDescription();
	
}
