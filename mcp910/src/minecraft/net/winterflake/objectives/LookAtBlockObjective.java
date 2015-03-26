package net.winterflake.objectives;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;

/**
 *
 * @author leijurv
 */
public class LookAtBlockObjective extends BaseObjective {
    final int x;
    final int y;
    final int z;
    public LookAtBlockObjective(int x, int y, int z) {
        super(1000);
        this.x = x;
        this.y = y;
        this.z = z;
    }
    @Override
    protected double calculateDifficulty() {
        return 1;//return getCurrentLookingDirection()-directionToLookAt(x,y,z)
    }
    public static void lookAtBlock(BlockPos p, EntityPlayerSP thePlayer){
    	double x=p.getX()+0.5;
		double z=p.getZ()+0.5;
		double y=p.getY()+0.5;
		double yDiff=(thePlayer.posY+1.62)-y;
	double yaw=Math.atan2(thePlayer.posX-x,-thePlayer.posZ+z);
	double dist=Math.sqrt((thePlayer.posX-x)*(thePlayer.posX-x)+(-thePlayer.posZ+z)*(-thePlayer.posZ+z));
	double pitch=Math.atan2(yDiff,dist);
	thePlayer.rotationYaw=(float)(yaw*180/Math.PI);
	thePlayer.rotationPitch=(float)(pitch*180/Math.PI);
    }
	@Override
	public void doTick(Minecraft mc) {
		lookAtBlock(new BlockPos(x,y,z),mc.thePlayer);
		
	}
}
