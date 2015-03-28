package net.winterflake.event;

import java.util.EventListener;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class EventManager {
	private static ArrayList<PlayerItemPickupEventListener> playerItemPickupListeners = new ArrayList<PlayerItemPickupEventListener>();
	
	public static void addListener(EventListener listener){
		if(listener instanceof PlayerItemPickupEventListener)
			playerItemPickupListeners.add((PlayerItemPickupEventListener) listener);
	}
	
	public static void handleEvent(Event event){
		if(event instanceof PlayerItemPickupEvent){
			for(PlayerItemPickupEventListener l : playerItemPickupListeners)
				l.eventPerformed((PlayerItemPickupEvent) event);
		}
	}
}
