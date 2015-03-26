package net.winterflake.objectives;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

/**
 *
 * @author leijurv
 */
public class GetToCraftingTableObjective extends Objective implements Parent {
	static boolean hasCraftingTable = true;
	private AquireItemObjective craftingtable;
	private BlockPos craftingTable;
	private MovementObjective ct;
	private boolean rightClicked = false;

	public GetToCraftingTableObjective() {
		new Thread() {
			public void run() {
				while (true) {

					try {
						Thread.sleep(500);
						craftingTable = findCraftingTable(
								(int) Objectives.mc.thePlayer.posX,
								(int) Objectives.mc.thePlayer.posY,
								(int) Objectives.mc.thePlayer.posZ,
								Objectives.mc);
						if (craftingTable != null) {
							Objectives.craftingTable = craftingTable;
							ct=new MovementObjective(10000,craftingTable.getX(),craftingTable.getY(),craftingTable.getZ());
							System.out.println("Finished searching");
							break;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
		if (!hasCraftingTable) {
			craftingtable = AquireItemObjective.getAquireItemObjective(
					new ItemStack(Blocks.crafting_table, 1), false);
		}
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof GetToCraftingTableObjective;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public double getDifficulty() {
		if (hasCraftingTable) {
			return 1;
		}
		System.out.println("oaeuaoeuaeoauoe");
		return craftingtable.getDifficulty();
	}

	@Override
	public double getPriority(Objective o) {
		if (hasChild(o)) {
			return getPriority();
		}
		return 0;
	}

	@Override
	public boolean hasChild(Objective child) {
		return craftingtable.equals(child);
	}

	@Override
	protected void doTick(Minecraft mc) {
		if (finished) {
			System.out.println("WHAT");
		}
		if (rightClicked) {
			if (mc.currentScreen instanceof GuiCrafting) {
				
				System.out.println("Finished with crafting");
				finished = true;
				rightClicked = false;
			}else{
				System.out.println(mc.currentScreen);
			}
			
			return;
		}
		if(ct!=null){
		if(!ct.onTick(mc)){
			System.out.println("Get wit fi");
			new RightClickObjective().onTick(mc);
			rightClicked=true;
			
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
					if (dank.getBlock().equals(Blocks.crafting_table)) {
						return bp;
					}
				}
			}
		}
		return null;
	}
}
