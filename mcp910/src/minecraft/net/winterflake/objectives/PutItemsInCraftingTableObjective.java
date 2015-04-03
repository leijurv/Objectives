package net.winterflake.objectives;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;

public class PutItemsInCraftingTableObjective extends Objective {
	
	final ShapedRecipes recipe;
	final int num;
	ArrayList<int[]> clicks = new ArrayList<int[]>();
	int clickPosition = 0;
	
	public PutItemsInCraftingTableObjective(ShapedRecipes r, int num) {
		recipe = r;
		this.num = num;
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
		if (clicks.isEmpty()) {
			calculateCliks(mc);
		}
		if (clickPosition % 3 == 0) {
			int click = clickPosition / 3;
			if (click >= clicks.size()) {
				finished = true;
				return;
			}
			if (runClick(mc, clicks.get(click))) {
				clickPosition++;
			}
		} else {
			clickPosition++;
		}
	}
	
	public boolean runClick(Minecraft mc, int[] click) {
		if (mc.currentScreen instanceof GuiCrafting) {
			GuiCrafting s = (GuiCrafting) (mc.currentScreen);
			s.handleMouseClick(null, click[0], click[1], click[2]);
			return true;
		} else {
			return false;
		}
	}
	
	public void calculateCliks(Minecraft mc) {
		System.out.println("CALCULATING");
		for (int i = 0; i < recipe.recipeItems.length; i++) {
			ItemStack it = recipe.recipeItems[i];
			if (it != null) {
				Item neededItem = it.getItem();
				InventoryPlayer a = mc.thePlayer.inventory;
				ItemStack[] stacks = a.mainInventory;
				boolean d = false;
				for (int j = 0; j < stacks.length; j++) {
					if (stacks[j] != null && stacks[j].getItem().equals(neededItem)) {
						
						clicks.add(new int[] { j + 37, 0, 0 });
						for (int k = 0; k < num; k++) {
							clicks.add(new int[] { map(i, recipe.recipeWidth, recipe.recipeHeight), 1, 0 });
						}
						clicks.add(new int[] { j + 37, 0, 0 });
						System.out.println(stacks[j] + " from " + j + " to " + i);
						d = true;
						break;
					}
				}
			}
		}
		clicks.add(new int[] { 0, 0, 1 });
	}
}
