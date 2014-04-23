package dd.Engine.CreatureEbents.Listener;

import java.util.List;

import dd.Actions.Default.Action;
import dd.Creature.Creature;

public interface GetPerformableActionsListener {
	
	public abstract void getPerformableActions(Creature c, List<Action> actions);
	
}
