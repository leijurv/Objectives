package net.winterflake.objectives;

import java.util.ArrayList;

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
	public static BlockPos craftingTableLocation;
	private static MovementObjective ct;
	private static ArrayList<GetToCraftingTableObjective> instanceList = new ArrayList<>();
	private boolean rightClicked = false;
	private Thread findingThread;

	public GetToCraftingTableObjective() {
		instanceList.add(this);
		if (findingThread == null) {
			findingThread = new Thread() {

				public void run() {
					while (true) {

						try {
							Thread.sleep(500);
							if (Objectives.mc.theWorld == null
									|| Objectives.mc.thePlayer == null || craftingTableLocation!=null)
								continue;
							BlockPos t = findCraftingTable(
									(int) Objectives.mc.thePlayer.posX,
									(int) Objectives.mc.thePlayer.posY,
									(int) Objectives.mc.thePlayer.posZ,
									Objectives.mc);
							if (t != null) {
								craftingTableLocation = t;
								ct = new MovementObjective(10000,
										craftingTableLocation.getX(),
										craftingTableLocation.getY(),
										craftingTableLocation.getZ());
								for(GetToCraftingTableObjective c : instanceList){
									ct.registerParent(c);
								}
								System.out.println("Finished searching"+t);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			};
			findingThread.start();
		}
		if(ct!=null){
			ct.registerParent(this);
		}
		if (!hasCraftingTable) {
			craftingtable = AquireItemObjective.getAquireItemObjective(
					new ItemStack(Blocks.crafting_table, 1), Need.MULTI);
			craftingtable.registerParent(this);
		}
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof GetToCraftingTableObjective;
	}

	@Override
	public int hashCode() {
		return 0;// All getToCraftingTable objectives are the same, so it
					// implements hashcode to satisfy the contract
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
		return child.equals(craftingtable) || child.equals(ct);
	}

	@Override
	protected void doTick(Minecraft mc) {
		if (finished) {
			System.out.println("WHAT");
		}
		if (craftingTableLocation != null) {
			IBlockState dank = mc.theWorld.getBlockState(craftingTableLocation);
			if (!dank.getBlock().equals(Blocks.crafting_table)) {
				craftingTableLocation = null;
				ct = null;
			}
		}

		if (mc.currentScreen instanceof GuiCrafting) {

			System.out.println("Finished with opening crafting");
			finished = true;
			rightClicked = false;
		} else {
			System.out.println("current screen" + mc.currentScreen);
		}
		if (rightClicked) {// If we right clicked and the screen isn't open yet,
							// just wait
			return;
		}
		if (ct != null) {
			if (!ct.onTick(mc)) {
				System.out.println("Right clicking crafting table");
				new RightClickObjective().onTick(mc);
				rightClicked = true;

			}
		}
	}

	/**
	 * Find a crafting table within 20 blocks of the specified coordinates
	 * 
	 * @param curX
	 *            x coordinate
	 * @param curY
	 *            y coordinate
	 * @param curZ
	 *            z coordinate
	 * @param mc
	 *            minecraft object
	 * @return a crafting table within 20 blocks (not necesarily closest), or
	 *         null if there aren't any
	 */
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
