package dd.Actions.Default;

import java.util.List;

import dd.Creature.Creature;
import dd.Engine.CreatureEvents;
import dd.Engine.Engine;
import dd.Engine.CreatureEbents.Listener.GetPerformableActionsListener;
import dd.Movement.Move;
import dd.Power.Power.ActionType;

public class Walk extends Action {
	
	static {
		
		
		CreatureEvents.addGetPerformableActionsListener(new GetPerformableActionsListener() {
			
			@Override
			public void getPerformableActions(Creature c, List<Action> actions) {
				actions.add(new Walk(c));
			}
		});
	}
	
	public Walk(Creature performer) {
		super(performer);
	}
	
	@Override
	public void execute() {
		
		Move move = new Move();
		
		move.move(performer, performer, performer.getSpeed(), this);
		
	}
	
	@Override
	public String getDescription() {
		return "Walk";
	}

	@Override
	public ActionType getType() {
		return ActionType.Move;
	}
	
}
