package net.winterflake.objectives;

import net.minecraft.client.Minecraft;

/**
 *
 * @author leijurv
 */
public class PlaceBlockObjective extends BaseObjective {
	
	public PlaceBlockObjective(long updatePeriod) {
		super(updatePeriod);
	}
	
	@Override
	protected double calculateDifficulty() {
		throw new UnsupportedOperationException("Not supported yet."); // To
																		// change
																		// body
																		// of
																		// generated
																		// methods,
																		// choose
																		// Tools
																		// |
																		// Templates.
	}
	
	@Override
	public void doTick(Minecraft mc) {
		
	}
}
