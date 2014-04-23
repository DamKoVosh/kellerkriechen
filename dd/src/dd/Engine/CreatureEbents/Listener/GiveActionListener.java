package dd.Engine.CreatureEbents.Listener;

import dd.Creature.ActionSet;
import dd.Creature.Creature;

public interface GiveActionListener {
	
	public ActionSet calculateActions(Creature c, ActionSet as);
	
}
