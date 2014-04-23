package dd.Engine.CreatureEbents.Listener;

import dd.Actions.Attack.Attack;
import dd.Creature.Creature;
import dd.Engine.CreatureEventListener;
import dd.Engine.Engine;

public abstract class EventHitListener extends CreatureEventListener {

	public final static String EVENT_HIT = "Hit";

	public EventHitListener() {
		super(EVENT_HIT);
	}

	@Override
	public void onEvent(Object... args) {
		if (args.length < 2) {
			Engine.log("Hit event error: too few arguments");
			return;
		}
		if (!(args[0] instanceof Attack)) {
			Engine.log("Hit event error: first argument not an Attack");
			return;
		}
		if (!(args[1] instanceof Creature)) {
			Engine.log("Hit event error: second argument not a Creature");
			return;
		}

		onHit((Attack) args[0], (Creature) args[1]);
	}

	public abstract void onHit(Attack attack, Creature target);

}
