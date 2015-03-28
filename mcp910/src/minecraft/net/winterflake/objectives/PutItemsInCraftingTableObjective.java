package net.winterflake.objectives;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;

public class PutItemsInCraftingTableObjective extends Objective {
	
	final ShapedRecipes recipe;
	
	public PutItemsInCraftingTableObjective(ShapedRecipes r) {
		recipe = r;
	}
	
	/**
	 * If the recipe isn't 3x3, then do some super sketchy math
	 * 
	 * @param id
	 *            position in crafting grid
	 * @param width
	 *            width of crafting grid
	 * @param height
	 *            height of crafting grid
	 * @return result of sketchy math
	 */
	public static int map(int id, int width, int height) {
		int yPos = id / width;
		int xPos = id % width;
		int z = xPos + 3 * yPos;
		System.out.println("Mapping " + id + " in " + width + "," + height + " to " + xPos + "," + yPos + " with id " + z);
		return z + 1;
	}
	
	@Override
	protected void doTick(Minecraft mc) {
		if (mc.currentScreen instanceof GuiCrafting) {
			GuiCrafting s = (GuiCrafting) (mc.currentScreen);
			finished = true;
			for (int i = 0; i < recipe.recipeItems.length; i++) {
				ItemStack it = recipe.recipeItems[i];
				if (it != null) {
					Item neededItem = it.getItem();
					InventoryPlayer a = mc.thePlayer.inventory;
					ItemStack[] stacks = a.mainInventory;
					boolean d = false;
					for (int j = 0; j < stacks.length; j++) {
						if (stacks[j] != null && stacks[j].getItem().equals(neededItem)) {
							
							s.handleMouseClick(null, j + 37, 0, 0);// Grab the
																	// item Do
																	// not
																	// question
																	// the j+37.
																	// And if
																	// you value
																	// your
																	// life, do
																	// not
																	// change
																	// it.
							s.handleMouseClick(null, map(i, recipe.recipeWidth, recipe.recipeHeight), 1, 0);// Put
																											// in
																											// table
																											// with
																											// right
																											// click
							s.handleMouseClick(null, j + 37, 0, 0);// Put
																	// leftovers
																	// back
																	// where
																	// they were
							System.out.println(stacks[j] + " from " + j + " to " + i);
							d = true;
							break;
						}
					}
					if (!d) {
						System.out.println("Didn't move blah" + i);
						finished = false;
					}
				}
			}
			if (finished) {
				s.handleMouseClick(null, 0, 0, 1);
				System.out.println("STUFFFF");
			}
			
		}
	}
	
}
