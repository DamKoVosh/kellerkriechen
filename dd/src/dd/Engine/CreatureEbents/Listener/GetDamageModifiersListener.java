package dd.Engine.CreatureEbents.Listener;

import dd.Actions.Attack.Attack;
import dd.Creature.Creature;
import dd.Engine.ModifierCollection;

public interface GetDamageModifiersListener {

	void getDamageModifiers(Creature target, Attack attack, ModifierCollection mod);
}
