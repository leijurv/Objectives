package net.winterflake.objectives;

import net.minecraft.client.Minecraft;

public class CloseObjective extends Objective{

	@Override
	protected void doTick(Minecraft mc) {
		mc.displayGuiScreen(null);
		finished=true;
	}

}
