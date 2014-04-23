package dd.Engine;

public abstract class CreatureEventListener {
	
	private String eventName;
	
	public CreatureEventListener(String eventName) {
		this.eventName = eventName;
	}
	
	public String getEventName() {
		return eventName;
	}
	
	public abstract void onEvent(Object... args);
	
}
