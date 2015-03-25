package net.winterflake.objectives;

import java.util.ArrayList;

import com.jcraft.jorbis.Block;

import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

/**
 *
 * @author leijurv
 */
public class Objectives {

	static public Minecraft mc;
	public static boolean isRightClick=false;
	public static int leftClickTick=0;
	public static boolean isLeftClick=true;
	public static boolean isJumping=false;
	public static boolean strafe=false;
	public static boolean forward=false;
	public static BlockPos craftingTable;
	public static TopLevelObjective main;
	public static void onTick(){
		if(isLeftClick)
			mc.clickMouse();
		//isLeftClick=false;
	main.onTick(mc);
	System.out.println(isLeftClick);
	}
	
	public static boolean isPressed()
    {
		if(7<10){
			return false;
		}
        if (leftClickTick <= 0)
        {
            return false;
        }
        else
        {
            --leftClickTick;
            return true;
        }
    }
    /**
     * @param args the command line arguments
     */
	public Objectives(Minecraft mcIn){
		mc = mcIn;
		/*
		new Thread(){
			public void run(){
			while(true){
				try{
					Thread.sleep(5000);
					System.out.println("Dropping");
				mc.thePlayer.dropOneItem(false);
				Thread.sleep(5000);
				System.out.println("Right");
			mc.rightClickMouse();
			Thread.sleep(5000);
			System.out.println("Left");
			long t=System.currentTimeMillis();
			isLeftClick=true;
			while (System.currentTimeMillis()<t+10000){
		Thread.sleep(100);
			}
			isLeftClick=false;
		Thread.sleep(5000);
		System.out.println("Jump");
		mc.thePlayer.movementInput.jump=true;
		mc.thePlayer.movementInput.jump=true;
		mc.thePlayer.movementInput.jump=true;
		
				}catch(Exception e){
					System.out.println(e);
				}
			
			}
			}
		}.start();*/
		/*
		new Thread(){
			public void run(){
				while(true){
					try {
						Thread.sleep(1000/60);
						if(craftingTable!=null){
							LookAtBlockObjective.lookAtBlock(craftingTable, mc.thePlayer);
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
			}
		}.start();*/
		ArrayList<Objective> dank=new ArrayList<Objective>();
		dank.add(new GetToCraftingTableObjective());
		dank.add(new DoMineBlockObjective(1,2,3,null));
		main=new TopLevelObjective(dank,1);
	}

}
