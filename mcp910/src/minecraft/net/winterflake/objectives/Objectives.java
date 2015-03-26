package net.winterflake.objectives;

import java.util.ArrayList;

import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

/**
 *
 * @author leijurv
 */
public class Objectives {

	static public Minecraft mc;
	public static boolean isRightClick = false;
	public static int pressTime = 0;
	public static boolean isLeftClick = false;
	public static boolean isJumping = false;
	public static boolean strafe = false;
	public static boolean forward = false;
	public static BlockPos craftingTable;
	public static TopLevelObjective main=null;
	public static boolean alr=false;
	public static boolean wasScreen=false;
	public static void onTick() {
		if(main==null){
			reset();
		}
		if(mc.theWorld==null || mc.thePlayer==null){
			return;
		}
		if(mc.currentScreen!=null){
			wasScreen=true;
		}else{
			if(wasScreen){
				wasScreen=false;
				pressTime=-10;
						
			}
		}
		if(pressTime<0){
			//System.out.println(pressTime);
		}
		if (isLeftClick && mc.currentScreen==null)
			pressTime++;
		
		 //if(isLeftClick) mc.clickMouse();
		//System.out.println(mc.currentScreen+","+(mc.currentScreen instanceof GuiCrafting));
		 if(mc.currentScreen instanceof GuiCrafting){
			 GuiCrafting s=(GuiCrafting)(mc.currentScreen);
			
			 if(!alr){
				 System.out.println("SlotClick");
				 s.handleMouseClick(null, 37, 0, 0);
				 s.handleMouseClick(null, 3, 0, 0);
				 alr=true;
			 }
		 }
		 if(mc.currentScreen==null){
			 alr=false;
		 }
		// isLeftClick=false;
		
		
		if (!main.onTick(mc)) {
			reset();
		}
		//System.out.println(isLeftClick + "," + pressTime);
		
		}
public static boolean getIsPressed(){
	return isLeftClick && mc.currentScreen==null && pressTime>-2;
}
	public static boolean isPressed() {
		if (pressTime <= 0) {
			return false;
		} else {
			--pressTime;
			return true;
		}
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public Objectives(Minecraft mcIn) {
		mc = mcIn;
		/*
		 * new Thread(){ public void run(){ while(true){ try{
		 * Thread.sleep(5000); System.out.println("Dropping");
		 * mc.thePlayer.dropOneItem(false); Thread.sleep(5000);
		 * System.out.println("Right"); mc.rightClickMouse();
		 * Thread.sleep(5000); System.out.println("Left"); long
		 * t=System.currentTimeMillis(); isLeftClick=true; while
		 * (System.currentTimeMillis()<t+10000){ Thread.sleep(100); }
		 * isLeftClick=false; Thread.sleep(5000); System.out.println("Jump");
		 * mc.thePlayer.movementInput.jump=true;
		 * mc.thePlayer.movementInput.jump=true;
		 * mc.thePlayer.movementInput.jump=true;
		 * 
		 * }catch(Exception e){ System.out.println(e); }
		 * 
		 * } } }.start();
		 */
		/*
		 * new Thread(){ public void run(){ while(true){ try {
		 * Thread.sleep(1000/60); if(craftingTable!=null){
		 * LookAtBlockObjective.lookAtBlock(craftingTable, mc.thePlayer); } }
		 * catch (InterruptedException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * 
		 * } } }.start();
		 */
	}

	public static void reset() {
		ArrayList<Objective> dank = new ArrayList<Objective>();
		dank.add(AquireItemObjective.getAquireItemObjective(new ItemStack(Items.wooden_pickaxe,1),true));
		//dank.add(new GetToCraftingTableObjective());
		//dank.add(new DoMineBlockObjective(1, 2, 3, null));
		main = new TopLevelObjective(dank, 1);
	}
}
