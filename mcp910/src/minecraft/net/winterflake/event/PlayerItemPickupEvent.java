package net.winterflake.event;
/**
 * 
 * @author avecowa
 *
 */
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerItemPickupEvent implements Event{

	final EntityItem i;
	final EntityPlayer p;
	
	public PlayerItemPickupEvent(EntityItem source, EntityPlayer target) {
		i = source;
		p = target;
	}
	
	public EntityItem getItem(){
		return i;
	}
	
	public EntityPlayer getPlayer(){
		return p;
	}

}
