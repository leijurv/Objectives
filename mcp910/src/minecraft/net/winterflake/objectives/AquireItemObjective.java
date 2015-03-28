package net.winterflake.objectives;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

/**
 * This class represents objectives that consist of obtaining items. This
 * objective can be used as a child objective or as a final objective.
 * 
 * @author leijurv
 * @author howardstark
 * @author ave4224
 */
public class AquireItemObjective extends HighPriorityMultiOrObjective {
	
	final Need need;
	final ItemStack item;
	final Claim claim;
	
	private AquireItemObjective(ItemStack itemstack, Need needtype) {
		super(howToGet(itemstack));// init
		this.item = itemstack;
		this.need = needtype;
		claim = new Claim(this);
	}
	
	/**
	 * A static method to create AquireItemObjectives
	 * 
	 * @param item
	 *            an ItemStack containing the type and amount needed
	 * @param need
	 *            whether the item will be used up (used in crafting), or can be
	 *            used multiple times (like a crafting table)
	 * @return
	 */
	public static AquireItemObjective getAquireItemObjective(ItemStack item, Need need) {
		if (item == null || item.getItem() == null || need == null)
			return null;
		return new AquireItemObjective(item, need);
	}
	
	@Override
	public double getDifficulty() {
		return (1 - getCompletionPercentage()) * super.getDifficulty();
	}
	
	/**
	 * What Objectives are possible ways to get the specified item
	 * 
	 * @param item
	 *            the item
	 * @return possible ways to get it. consists of newly instantiated
	 *         objectives.
	 */
	public static ArrayList<Objective> howToGet(ItemStack item) {
		ArrayList<Objective> possibilities = new ArrayList<Objective>();
		List<IRecipe> l = CraftingManager.getInstance().getRecipeList();
		for (IRecipe r : l) {
			if (r != null && r.getRecipeOutput() != null) {
				// System.out.println(r+","+r.getRecipeOutput().getItem()+item.getItem()+","+item.getItem().equals(r.getRecipeOutput().getItem()));
				if (Item.getIdFromItem(item.getItem()) == Item.getIdFromItem(r.getRecipeOutput().getItem())) {
					possibilities.add(new CraftItemObjective(item, r));
					return possibilities;
				}
			}
		}
		// TODO have some system for custom ones. IDK how that could work
		System.out.println(Block.getIdFromBlock(Blocks.log) + " " + Item.getIdFromItem(item.getItem()) + " " + item + " " + item.getItem());
		if (Block.getIdFromBlock(Blocks.log) == Item.getIdFromItem(item.getItem())) {
			possibilities.add(new PunchTreeObjective());
		}
		System.out.println(item + " has options " + possibilities);
		return possibilities;
	}
	
	/**
	 * What percentage done is this aquireitemobjective
	 * 
	 * @return the completion, on a range of from 0 to 1
	 */
	public double getCompletionPercentage() {
		return ((double) claim.getAmountCompleted()) / ((double) item.stackSize);
	}
	
	public boolean onTick(Minecraft mc) {
		if (checkFinished(mc, item)) {
			finished = true;
		}
		return super.onTick(mc);
	}
	
	public boolean checkFinished(Minecraft mc, ItemStack item) {
		if (mc.thePlayer == null)
			return true;
		return claim.getAmountCompleted() == item.stackSize;
	}
	
}

/**
 * This enum represents whether the required item for an
 * <code>AquireItemObjective</code> will be used up (used in crafting), or can
 * be used multiple times (like a crafting table)
 *
 */
enum Need {
	MULTI, // Can be used multiple times, like a furnace or crafting table
	SINGLE // Will be used up, like an iron ingot
}