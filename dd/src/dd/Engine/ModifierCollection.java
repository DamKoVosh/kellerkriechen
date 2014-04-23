package dd.Engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ModifierCollection {
	
	private Map<String, Integer> mods = new HashMap<String, Integer>();
	
	public void add(String type, int value) {
		
		if (mods.containsKey(type)) {
			if (type == "") {
				value = value + mods.get(type);
			} else {
				value = Math.max(value, mods.get(type));
			}
		}
		
		mods.put(type, value);
	}
	
	public int getTotalValue() {
		int val = 0;
		for (Entry<String, Integer> set : mods.entrySet()) {
			val += set.getValue();
		}
		
		return val;
	}
	
}
