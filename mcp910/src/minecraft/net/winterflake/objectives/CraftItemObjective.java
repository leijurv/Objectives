package net.winterflake.objectives;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

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
		if (requiresCraftingTable(recipe)) {
			System.out.println("Requires c");
			input.add(new GetToCraftingTableObjective());
		}
		return input;
	}

	public static boolean requiresCraftingTable(IRecipe recipe) {
		return recipe.getRecipeSize() > 4;
	}

	@Override
	public double getPriority(Objective o) {
		throw new UnsupportedOperationException("Not supported yet."); // To
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
