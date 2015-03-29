package net.winterflake.event;
/**
 * 
 * @author avecowa
 *
 */
import net.minecraft.util.DamageSource;

public class PlayerDamagedEvent implements Event{

	final DamageSource s;
	final float d;
	
	public PlayerDamagedEvent(DamageSource source, float damage) {
		s = source;
		d = damage;
	}
	
	public DamageSource getSource(){
		return s;
	}
	
	public float getDamage(){
		return d;
	}
	
}
