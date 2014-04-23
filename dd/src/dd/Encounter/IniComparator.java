package dd.Encounter;

import java.util.Comparator;

public class IniComparator implements Comparator<InitiativeEntry> {

	@Override
	public int compare(InitiativeEntry o1, InitiativeEntry o2) {
		if (o1.ini < o2.ini) {
			return -1;
		} else if (o1.ini > o2.ini) {
			return 1;
		} else {
			int iniBase1, iniBase2;
			iniBase1 =o1.creature.getInitiative();
			iniBase2 =o2.creature.getInitiative();
			if (iniBase1 < iniBase2) {
				return -1;
			} else if (iniBase1 > iniBase2) {
				return 1;
			} else {
				return (Roll.d(2) == 1) ? -1 : 1;
			}
		}
		
	}


	
}
