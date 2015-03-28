package net.winterflake.event;

import java.util.EventListener;

public interface PlayerItemPickupEventListener extends EventListener{
	void eventPerformed(PlayerItemPickupEvent event);
}