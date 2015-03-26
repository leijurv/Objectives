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
 *
 * @author leijurv
 */
public class AquireItemObjective extends HighPriorityMultiOrObjective {

	final ItemStack item;
	final Claim claim;

	private AquireItemObjective(ItemStack item) {
		super(howToGet(item));
		this.item = item;
		claim = new Claim(this);
	}

	private static final HashMap<Item, AquireItemObjective> statics = new HashMap<Item, AquireItemObjective>();

	/**
	 * A static method to create AquireItemObjectives
	 * 
	 * @param itemID
	 *            the item id
	 * @param amount
	 *            the amount
	 * @param need
	 *            whether the item will be used up (used in crafting), or can be
	 *            used multiple times (like a crafting table)
	 * @return
	 */
	public static AquireItemObjective getAquireItemObjective(ItemStack item,
			boolean need) {
		if (need) {
			return new AquireItemObjective(item);
		}
		if (statics.get(item.getItem()) != null) {
			return statics.get(item.getItem());
		}
		AquireItemObjective o = new AquireItemObjective(item);
		statics.put(item.getItem(), o);
		return o;
	}

	@Override
	public double getDifficulty() {
		return (1 - getCompletionPercentage()) * super.getDifficulty();
	}

	public static ArrayList<Objective> howToGet(ItemStack item) {
		ArrayList<Objective> possibilities = new ArrayList<Objective>();
		if (checkFinished(Objectives.mc, item)) {
			return possibilities;
		}
		List<IRecipe> l = CraftingManager.getInstance().getRecipeList();
		for (IRecipe r : l) {
			if (r != null && r.getRecipeOutput() != null) {
				// System.out.println(r+","+r.getRecipeOutput().getItem()+item.getItem()+","+item.getItem().equals(r.getRecipeOutput().getItem()));
				if (Item.getIdFromItem(item.getItem()) == Item.getIdFromItem(r
						.getRecipeOutput().getItem())) {
					possibilities.add(new CraftItemObjective(item, r));
					return possibilities;
				}
			}
		}
		System.out.println(Block.getIdFromBlock(Blocks.log)+" "+Item.getIdFromItem(item.getItem())+" "+item+" "+item.getItem());
		if(Block.getIdFromBlock(Blocks.log)==Item.getIdFromItem(item.getItem())){
			possibilities.add(new PunchTreeObjective());
		}
		System.out.println(item + " has options " + possibilities);
		return possibilities;
	}

	public double getCompletionPercentage() {
		return ((double) claim.getAmountCompleted())
				/ ((double) item.stackSize);
	}

	public boolean onTick(Minecraft mc) {
		if (checkFinished(mc, item)) {
			finished = true;
		}
		return super.onTick(mc);
	}

	public static boolean checkFinished(Minecraft mc, ItemStack item) {
		if (mc.thePlayer == null) {
			return true;
		}
		for (ItemStack a : mc.thePlayer.inventory.mainInventory) {
			if (a != null) {
				if (Item.getIdFromItem(item.getItem()) == Item.getIdFromItem(a
						.getItem()) && item.stackSize<=a.stackSize) {
					System.out.println("Finished because already has " + item);
					return true;
				}
			}
		}
		return false;
	}

}
