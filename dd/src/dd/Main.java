package dd;

import dd.Creature.Creature;
import dd.Creature.Kaspar.KruthikYoung;
import dd.Creature.Kaspar.Rangrim;
import dd.Creature.Kaspar.Rimas;
import dd.Encounter.Encounter;
import dd.Engine.Engine;
import dd.UI.Window;

public class Main {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Engine.init();
		
		Board b = new Board();
		
		Window w = new Window(b);
		
		Engine.setWindow(w);
		Engine.setBoard(b);
		
		String str;
		str = b.getObjectAtCoordinates(1, 1).toString();
		System.out.println("At coords: " + str);
		
		Creature hero = new Rangrim();
		hero.setPosition(5, 2);
		b.addObject(hero);
		
		Creature kruthik = new KruthikYoung();
		kruthik.setPosition(8, 6);
		b.addObject(kruthik);
		
		Creature kruthik2 = new KruthikYoung();
		kruthik2.setPosition(8, 7);
		b.addObject(kruthik2);
		kruthik2.setName("Kruthik2");
		
		Encounter e = new Encounter();
		e.combatants.add(hero);
		e.combatants.add(kruthik);
		e.combatants.add(kruthik2);
		
		e.start();
		
	}
	
}
