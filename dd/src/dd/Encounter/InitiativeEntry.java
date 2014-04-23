package dd.Encounter;

import dd.Creature.Creature;

public class InitiativeEntry {
	int ini;
	Creature creature;
	
	InitiativeEntry(int ini, Creature c) {
		this.ini = ini;
		this.creature = c;
	}
}
