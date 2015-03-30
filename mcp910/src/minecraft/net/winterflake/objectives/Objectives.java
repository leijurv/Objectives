package net.winterflake.objectives;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.winterflake.event.EventManager;

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
	public static TopLevelObjective main = null;
	public static boolean alr = false;
	public static boolean wasScreen = false;
	public static long lastReset = 0;
	public static long tickIndex = 0;
	
	public static void onTick() {
		
		if (mc.theWorld == null || mc.thePlayer == null)
			return;
		if (main == null)
			reset();
		if (mc.currentScreen != null) {
			wasScreen = true;
		} else {
			if (wasScreen) {
				wasScreen = false;
				pressTime = -10;
				
			}
		}
		tickIndex++;
		if (tickIndex % 10 == 0) {
			System.out.println("UPDATING CLAIMS");
			AquireItemObjective.updateClaims();
		}
		
		if (isLeftClick && mc.currentScreen == null)
			pressTime++;
		
		if (!main.onTick(mc)) {
			/*
			 * if (lastReset + 1000 < System.currentTimeMillis()) { reset();
			 * lastReset = System.currentTimeMillis();
			 * System.out.println("RESETTING"); }
			 */
		}
		// System.out.println(isLeftClick + "," + pressTime);
		
	}
	
	/**
	 * Do not question the logic
	 * 
	 * @return
	 */
	public static boolean getIsPressed() {
		return isLeftClick && mc.currentScreen == null && pressTime > -2;
	}
	
	/**
	 * Do not question the logic
	 * 
	 * @return
	 */
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
		
		EventManager.addListener(new ClaimListener());
		
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
		
		craftingTable = null;
		ArrayList<Objective> dank = new ArrayList<Objective>();
		
		dank.add(AquireItemObjective.getAquireItemObjective(new ItemStack(Items.stone_pickaxe, 1), Need.SINGLE));
		// dank.add(new GetToCraftingTableObjective());
		dank.add(new DoMineBlockObjective(1, 2, 3, null));
		// dank.add(AquireItemObjective.getAquireItemObjective(new
		// ItemStack(Blocks.log,1),true));
		main = new TopLevelObjective(dank, 1);
	}
}
