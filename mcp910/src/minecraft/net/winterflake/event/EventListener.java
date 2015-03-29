package net.winterflake.event;

/**
 * 
 * @author avecowa
 *
 */
public abstract class EventListener implements java.util.EventListener {
	
	protected Event event;
	
	public abstract Class getEventType();
	
	protected abstract void onEvent();
	
	public final boolean eventTriggered(Event e) {
		if (!getEventType().isInstance(e))
			throw new ClassCastException("From " + e.getClass().toString() + " to " + getEventType().toString());
		event = e;
		final EventListener l = this;
		new Thread() {
			
			public void run() {
				l.onEvent();
			}
		}.start();
		return true;
	}
}
