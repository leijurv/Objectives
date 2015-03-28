package net.winterflake.objectives;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;

/**
 *
 * @author leijurv
 */
public class CraftItemObjective extends MultiAndObjective {
	
	final ItemStack item;
	final IRecipe recipe;
	
	public CraftItemObjective(ItemStack item, IRecipe recipe) {
		super(getRequirements(recipe, item));
		this.recipe = recipe;
		this.item = item;
	}
	
	public static ArrayList<Objective> getRequirements(IRecipe recipe, ItemStack item) {
		ArrayList<Objective> input = new ArrayList<>();
		int stackSize = item.stackSize;
		if (recipe instanceof ShapedRecipes) {
			ShapedRecipes n = (ShapedRecipes) recipe;
			ArrayList<Item> items = new ArrayList<Item>();
			ArrayList<Integer> amounts = new ArrayList<Integer>();
			for (ItemStack r : n.recipeItems) {
				if (r != null) {
					Item i = r.getItem();
					int itemID = Item.getIdFromItem(i);
					int amount = r.stackSize;
					boolean d = false;
					for (int j = 0; j < items.size(); j++) {
						if (Item.getIdFromItem(items.get(j)) == itemID) {
							amounts.set(j, amounts.get(j) + stackSize);
							d = true;
							break;
						}
					}
					if (!d) {
						items.add(i);
						amounts.add(stackSize);
					}
				}
			}
			for (int j = 0; j < items.size(); j++) {
				input.add(AquireItemObjective.getAquireItemObjective(new ItemStack(items.get(j), amounts.get(j)), Need.SINGLE));
			}
			
		}
		if (requiresCraftingTable(recipe)) {
			System.out.println("Requires c");
			input.add(new GetToCraftingTableObjective());
		}
		input.add(new PutItemsInCraftingTableObjective((ShapedRecipes) recipe));
		input.add(new CloseObjective());
		System.out.println(input);
		return input;
	}
	
	public static boolean requiresCraftingTable(IRecipe recipe) {
		if (recipe.getRecipeSize() > 4)
			return true;
		return false;
	}
	
	@Override
	public double getPriority(Objective o) {
		return 1; // To
					// change
					// body
					// of
					// generated
					// methods,
					// choose
					// Tools
					// |
					// Templates.
	}
}
