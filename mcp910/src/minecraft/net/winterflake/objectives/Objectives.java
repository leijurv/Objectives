package net.winterflake.objectives;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;

/**
 *
 * @author leijurv
 */
public class Objectives {

	static public Minecraft mc;
	public static boolean isRightClick=false;
	public static int leftClickTick=0;
	public static boolean isLeftClick=false;
	public static boolean isJumping=false;
	public static boolean strafe=false;
	public static boolean forward=false;
	public static void onTick(){
		if(isLeftClick)
		leftClickTick++;
		strafe=(System.currentTimeMillis()/1000)%5==0;
		forward=(System.currentTimeMillis()/1000)%4==0;
		mc.thePlayer.rotationYaw= ((((float)(System.currentTimeMillis()))/100F)%(360F));
	}
	public static boolean isPressed()
    {
        if (leftClickTick == 0)
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
		}.start();
		
	}

}
