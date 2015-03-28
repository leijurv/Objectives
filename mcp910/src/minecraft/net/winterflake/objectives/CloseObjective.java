package net.winterflake.objectives;

import net.minecraft.client.Minecraft;

public class CloseObjective extends Objective {
	
	/**
	 * Close the current GUI screen. Used at the end of a craftitemobjective
	 */
	@Override
	protected void doTick(Minecraft mc) {
		mc.displayGuiScreen(null);
		finished = true;
	}
	
}
