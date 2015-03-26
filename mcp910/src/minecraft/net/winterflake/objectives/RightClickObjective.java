package net.winterflake.objectives;

import net.minecraft.client.Minecraft;

public class RightClickObjective extends Objective{

	@Override
	protected void doTick(Minecraft mc) {
		mc.rightClickMouse();
		finished=true;
	}

}
