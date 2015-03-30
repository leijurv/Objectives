package net.winterflake.objectives;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;

/**
 *
 * @author leijurv
 */
public class CraftItemObjective extends Objective implements Parent {
	
	final ItemStack item;
	final IRecipe recipe;
	final ArrayList<AquireItemObjective> inputs;
	final PutItemsInCraftingTableObjective put;
	final GetToCraftingTableObjective gtc;
	private volatile boolean hasAllInputs = false;
	private volatile boolean isInCraftingTable = false;
	
	public CraftItemObjective(ItemStack item, IRecipe recipe) {
		this.recipe = recipe;
		this.item = item;
		int stackSize = item.stackSize;
		int multiplier = item.stackSize / recipe.getRecipeOutput().stackSize;
		if (recipe instanceof ShapedRecipes) {
			inputs = new ArrayList<AquireItemObjective>();
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
							amounts.set(j, amounts.get(j) + amount * multiplier);
							d = true;
							break;
						}
					}
					if (!d) {
						items.add(i);
						amounts.add(amount * multiplier);
					}
				}
			}
			for (int j = 0; j < items.size(); j++) {
				inputs.add(AquireItemObjective.getAquireItemObjective(new ItemStack(items.get(j), amounts.get(j)), Need.SINGLE));
			}
			
		} else {
			inputs = null;
		}
		if (requiresCraftingTable(recipe) || true) {// Do not remove for now,
													// until we have crafting in
													// the 2x2 inventory
													// crafting grid working
			System.out.println("Requires c");
			gtc = new GetToCraftingTableObjective();
		}
		
		put = new PutItemsInCraftingTableObjective((ShapedRecipes) recipe, multiplier);
	}
	
	/**
	 * Does this recipe require a crafting table?
	 * 
	 * @param recipe
	 *            The recipe
	 * @return Whether it needs a crafting table
	 */
	public static boolean requiresCraftingTable(IRecipe recipe) {
		if (recipe.getRecipeSize() > 4)
			return true;
		return false;
	}
	
	@Override
	public double getPriority(Objective obj) {
		if (!hasChild(obj) || obj.isFinished()) {
			return 0;
		}
		if (!hasAllInputs) {
			int numUnfinished = 0;
			for (AquireItemObjective o : inputs) {
				if (!o.isFinished()) {
					numUnfinished++;
				}
			}
			return getPriority() / numUnfinished;// Divvy up equally among
													// unfinished
		}
		return getPriority();
	}
	
	@Override
	public boolean hasChild(Objective child) {
		return gtc.equals(child) || put.equals(child) || inputs.contains(child);
	}
	
	@Override
	protected void doTick(Minecraft mc) {
		if (isInCraftingTable) {
			if (!put.isFinished()) {
				put.onTick(mc);
				return;
			}
			// At this point we have put everything in the table, and taken the
			// result out
			new CloseObjective().onTick(mc);// Close the table =)
			finished = true;
			return;
		}
		for (AquireItemObjective o : inputs) {
			if (!o.isFinished()) {
				hasAllInputs = false;
				o.onTick(mc);
				return;
			}
		}
		hasAllInputs = true;
		// By this point we know that we have all the inputs in the inventory
		if (!gtc.isFinished()) {
			gtc.onTick(mc);
			return;
		}
		// By this point we have all the items in our inventory and we have the
		// crafting table open
		isInCraftingTable = true;
		for (AquireItemObjective o : inputs) {
			o.onUsedUp();// Once we start putting items in the crafting table we
							// don't want the input objectives to start freaking
							// out
		}
	}
	
}
