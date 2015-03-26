package net.winterflake.objectives;

import net.minecraft.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.winterflake.objectives.Objectives;

/**
 *
 * @author leijurv
 */
public class MovementObjective extends BaseObjective {

	Minecraft mc = Objectives.mc;

	final double targetX;
	final double targetY;
	final double targetZ;
	final BlockPos craftingTable;
	public static final double MC_WALKING_SPEED = 4.3;// blocks/sec
	public static final double MC_STAIR_ASCENDING_SPEED = 2;
	final boolean withinRange;

	/**
	 *
	 * @param updatePeriod
	 *            How often the difficulty should be recalculated
	 * @param x
	 *            xCoord
	 * @param y
	 *            yCoord
	 * @param z
	 *            zCoord
	 * @param withinRange
	 *            Whether you need to get within range of the block, or actually
	 *            to the block
	 */
	public MovementObjective(long updatePeriod, double x, double y, double z,
			boolean withinRange) {
		super(updatePeriod);
		this.targetX = x;
		this.targetY = y;
		this.targetZ = z;
		this.withinRange = withinRange;
		this.craftingTable=new BlockPos(x,y,z);
	}

	public MovementObjective(long updatePeriod, double x, double y, double z) {
		this(updatePeriod, x, y, z, false);
	}

	@Override
	protected double calculateDifficulty() {
		double curX = mc.thePlayer.posX;
		double curY = mc.thePlayer.posY;
		double curZ = mc.thePlayer.posZ;
		double XDiff = Math.abs(curX - targetX);
		double YDiff = Math.abs(curY - targetY);
		double ZDiff = Math.abs(curZ - targetZ);
		if (withinRange) {
			XDiff -= 4;
		}
		return (Math.sqrt(XDiff * XDiff + ZDiff * ZDiff) - YDiff)
				* MC_WALKING_SPEED + YDiff * MC_STAIR_ASCENDING_SPEED;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof MovementObjective) {
			MovementObjective m = (MovementObjective) o;
			return m.targetX == targetX && m.targetY == targetY
					&& m.targetZ == targetZ && m.withinRange == withinRange;
		}
		return false;
	}

	@Override
	public void doTick(Minecraft mc) {
		System.out.println("Doing tick");
		Objectives.forward = false;
		if (craftingTable != null) {
			EntityPlayerSP thePlayer = Objectives.mc.thePlayer;
			LookAtBlockObjective.lookAtBlock(craftingTable,
					Objectives.mc.thePlayer);

			if (mc.objectMouseOver != null
					&& mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
					&& mc.objectMouseOver.func_178782_a() != null) {
				BlockPos var9 = mc.objectMouseOver.func_178782_a();
				if (var9.equals(craftingTable)) {
					System.out.println("Done");
					finished=true;
					Objectives.forward = false;
				}
			} else {
				Objectives.forward = true;
			}
		}

	}
}
