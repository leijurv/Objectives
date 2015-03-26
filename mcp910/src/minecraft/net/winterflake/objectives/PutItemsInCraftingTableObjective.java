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
			 for(int i=0; i<9; i++){
				 ItemStack it=recipe.recipeItems[i];
				 if(it!=null){
				 Item neededItem=it.getItem();
				 InventoryPlayer a=mc.thePlayer.inventory;
				 ItemStack[] stacks=a.mainInventory;
				 for(int j=0; j<stacks.length; j++){
					 if(stacks[j]!=null && stacks[j].getItem().equals(neededItem)){
						 s.handleMouseClick(null, j+10, 0, 0);
						 s.handleMouseClick(null,i+1,1,0);
						 s.handleMouseClick(null,j+10,0,0);
						 finished=true;
					 }
				 }
				 }
			 }
		 }
	}
	

}
