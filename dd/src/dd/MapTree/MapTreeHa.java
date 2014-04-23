package dd.MapTree;

import java.util.List;

import dd.MapObjects.MapObject;


public class MapTreeHa extends MapTreeObject {
	private List<MapObject> objects;
	
	public MapTreeHa(List<MapObject> objects) {
		this.objects = objects;
	}
	
	public List<MapObject> getObjects() {
		return objects;
	}

	@Override
	public void addObject(MapObject obj) {
		objects.add(obj);
	}

	@Override
	public void removeObject(MapObject obj) {
		objects.remove(obj);
	}

	
}
