package net.winterflake.objectives;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;

/**
 *
 * @author leijurv
 */
public class Objectives {

	static public Minecraft mc;
	
    /**
     * @param args the command line arguments
     */
	public Objectives(Minecraft mcIn){
		mc = mcIn;
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
		mc.clickMouse();
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
		}.start();
		new Thread(){
			public void run(){
				while(true){
					try{
						Thread.sleep(5);
						mc.thePlayer.isJumping=true;
					}catch(Exception e){
					}
				}
			}
		}.start();
		
	}

}
