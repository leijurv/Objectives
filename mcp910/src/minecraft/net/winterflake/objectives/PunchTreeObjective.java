package net.winterflake.objectives;

import java.util.Random;

import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

public class PunchTreeObjective extends Objective{
	static BlockPos closestTree;
	static MovementObjective ct;
	boolean isNearTree=false;

static{
	new Thread() {
		public void run() {
			while (true) {

				try {
					Thread.sleep(500);
					if(closestTree!=null){
						continue;
					}
					BlockPos t = findCraftingTable(
							(int) Objectives.mc.thePlayer.posX,
							(int) Objectives.mc.thePlayer.posY,
							(int) Objectives.mc.thePlayer.posZ,
							Objectives.mc);
					if (t != null) {
						closestTree = t;
						ct = new MovementObjective(10000,
								closestTree.getX(), closestTree.getY(),
								closestTree.getZ(),false);
						System.out.println("Finished searching");
						
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}.start();
}
	@Override
	protected void doTick(Minecraft mc) {
		if (finished) {
			System.out.println("WHAT");
		}
		if(isNearTree){
			Objectives.isLeftClick = true;

			LookAtBlockObjective.lookAtBlock(closestTree,
					Objectives.mc.thePlayer);

			IBlockState dank = mc.theWorld.getBlockState(closestTree);
			if (dank.getBlock().equals(Blocks.air)) {
				System.out.println("Finished");
				Objectives.isLeftClick = false;
				finished = true;
				closestTree=null;
				isNearTree=false;
				ct=null;
			} else {
				// System.out.println(dank.getBlock());
			}
			return;
		}
		if (ct != null) {
			if (!ct.onTick(mc)) {
				System.out.println("Get wit fi");
				isNearTree=true;
			}
		}
		
	}
	public static BlockPos findCraftingTable(int curX, int curY, int curZ,
			Minecraft mc) {
		int s = 20;
		for (int x = curX - s; x <= curX + s; x++) {
			for (int y = curY - s; y <= curY + s; y++) {
				for (int z = curZ - s; z <= curZ + s; z++) {
					BlockPos bp = new BlockPos(x, y, z);
					IBlockState dank = mc.theWorld.getBlockState(bp);
					if (dank.getBlock().equals(Blocks.log)) {
						return bp;
					}
				}
			}
		}
		return null;
	}
}
