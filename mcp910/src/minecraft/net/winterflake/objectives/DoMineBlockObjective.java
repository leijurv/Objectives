package net.winterflake.objectives;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

/**
 *
 * @author leijurv
 */
public class DoMineBlockObjective extends BaseObjective {
	final int x;
	final int y;
	final int z;
	final AquireItemObjective item;

	public DoMineBlockObjective(int x, int y, int z, AquireItemObjective item) {
		super(10000000);
		this.x = x;
		this.y = y;
		this.z = z;
		this.item = item;
	}

	@Override
	protected double calculateDifficulty() {
		// Return getBlock(x,y,z).getAmountofTimetoMineWithTool(itemID)
		return 10;
	}

	@Override
	public void doTick(Minecraft mc) {
		Objectives.isLeftClick = true;
		BlockPos bp = Objectives.craftingTable;
		IBlockState dank = mc.theWorld.getBlockState(bp);
		if (dank.getBlock().equals(Blocks.air)) {
			System.out.println("Finished");
			Objectives.isLeftClick = false;
			finished = true;
		} else {
			System.out.println(dank.getBlock());
		}
	}
}
