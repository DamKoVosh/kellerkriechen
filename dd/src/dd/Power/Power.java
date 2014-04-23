package dd.Power;

import java.util.LinkedList;
import java.util.List;

import dd.Actions.Default.Action;
import dd.Creature.Creature;
import dd.Engine.Engine;

public abstract class Power {
	
	public enum ActionType {
		Standard, Move, Minor, Opportunity, ImmediateReaction, ImmediateInterrupt, Free, NoAction
	}
	
	protected Creature performer;
	
	private List<Action> reactions = new LinkedList<Action>();
	
	public Power() {
	}
	
	public void setPerformer(Creature performer) {
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
	
	public void perform(Creature performer) {
		
		this.performer = performer;
		
		reactions.clear();
		
		if (!performer.canPerformPower(this)) {
			Engine.log("%s can't perform action %s", performer.getName(),
					this.getDescription());
			return;
		}
		
		execute();
		
		if (getType() == ActionType.Standard) {
			performer.avaiableActions.standard--;
		} else if (getType() == ActionType.Move) {
			performer.avaiableActions.move--;
		} else if (getType() == ActionType.Minor) {
			performer.avaiableActions.minor--;
		}
		
		performReactions();
		
		
		Engine.getWindow().updateMap();
	}
	
	protected abstract void execute();
	
	public abstract String getDescription();
	
	public abstract ActionType getType();
}
