package dd.Engine.CreatureEbents.Listener;

import dd.Actions.Attack.Attack;
import dd.Creature.Creature;
import dd.Engine.ModifierCollection;

public interface GetAttackModifiersListener {

	void getAttackModifiers(Creature target, Attack attack, ModifierCollection mod);
}
