package net.winterflake.objectives;

import java.util.ArrayList;

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

	public static ArrayList<Objective> getRequirements(IRecipe recipe,
			ItemStack item) {
		ArrayList<Objective> input = new ArrayList<>();
		if (recipe instanceof ShapedRecipes) {
			ShapedRecipes n = (ShapedRecipes) recipe;
			for (ItemStack r : n.recipeItems) {
				if (r != null)
					input.add(AquireItemObjective.getAquireItemObjective(r,
							true));
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
		return true;
		// return recipe.getRecipeSize() >= 4;
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
