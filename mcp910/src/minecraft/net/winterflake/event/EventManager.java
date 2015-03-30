package net.winterflake.event;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * 
 * @author avecowa
 *
 */
public abstract class EventManager {
	private static HashMap<Class, ArrayList<EventListener>> listeners = new HashMap<Class, ArrayList<EventListener>>();
	
	public static void addListener(EventListener listener){
		Class c = listener.getEventType();
		if(listeners.get(c) == null)
			listeners.put(c, new ArrayList<EventListener>());
		listeners.get(c).add(listener);
	}
	
	public static void handleEvent(Event event){
		if(event == null)
			return;
		System.out.println(event+"\n"+listeners);
		ArrayList<EventListener> ls = listeners.get(event.getClass());
		if(ls == null)
			return;
		for(EventListener l : ls)
			l.eventTriggered(event);
	}
}
