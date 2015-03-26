package net.winterflake.objectives;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;

public class PutItemsInCraftingTableObjective extends Objective{
	final ShapedRecipes recipe;
	public PutItemsInCraftingTableObjective(ShapedRecipes r){
		recipe=r;
	}
	@Override
	protected void doTick(Minecraft mc) {
		if(mc.currentScreen instanceof GuiCrafting){
			 GuiCrafting s=(GuiCrafting)(mc.currentScreen);
			 finished=true;
			 boolean small=recipe.recipeItems.length==2;
			 for(int i=0; i<(small?2:9); i++){
				 ItemStack it=recipe.recipeItems[i];
				 if(it!=null){
				 Item neededItem=it.getItem();
				 InventoryPlayer a=mc.thePlayer.inventory;
				 ItemStack[] stacks=a.mainInventory;
				 boolean d=false;
				 for(int j=0; j<stacks.length; j++){
					 if(stacks[j]!=null && stacks[j].getItem().equals(neededItem)){
						 s.handleMouseClick(null, j+37, 0, 0);
						 s.handleMouseClick(null,i+1+((small&&i>0)?2:0),1,0);
						 s.handleMouseClick(null,j+37,0,0);
						 System.out.println(stacks[j]+" from "+j+" to "+i);
						 d=true;
						 break;
					 }
				 }
				 if(!d){
					 System.out.println("Didn't move blah"+i);
					 finished=false;
				 }
				 }
			 }
			 if(finished){
				 s.handleMouseClick(null,0,0,1);
				 System.out.println("STUFFFF");
			 }
			
		 }
	}
	

}
