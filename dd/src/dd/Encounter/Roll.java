package dd.Encounter;

import java.util.Random;

public class Roll {
	private static Random rand = new Random();
	
	public static int r (String expr) {
		String[] summanden = expr.split("\\+");
		
		int sum = 0;
		for (String s : summanden) {
			if (s.contains("d")) {
				String[] die;
				die = s.split("d");
				
				int d, count;
				count = Integer.valueOf(die[0].trim());
				d = Integer.valueOf(die[1].trim());
				
				sum += d(count, d);
			} else {
				sum += Integer.valueOf(s.trim());
			}
		}
		
		return sum;
	}
	
	public static int d6() {
		return d(6);
	}
	
	public static int d20() {
		return d(20);
	}
	
	public static int d(int d) {
		return d(1,d);
	}
	
	public static int d(int count, int d) {
		int sum = 0;
		for (int i = 0; i < count; i++) {
			sum +=  rand.nextInt(d) + 1;
		}
		return sum;
	}
}
