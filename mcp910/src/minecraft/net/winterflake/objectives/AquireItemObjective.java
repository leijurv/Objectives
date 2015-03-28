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
 *This class represents objectives that consist of obtaining items. This objective can be used as a child objective or as a final objective.
 * 
 * @author Leif Jurvetson
 * @author Mickey Daras
 * @author Avery Cowan
 */
public class AquireItemObjective extends HighPriorityMultiOrObjective {
	
	final Need need;
	final ItemStack item;
	final Claim claim;
	static final HashMap<Item, ArrayList<AquireItemObjective>> statics = new HashMap<Item, ArrayList<AquireItemObjective>>();
	
	private AquireItemObjective(ItemStack itemstack, Need needtype) {
		super(howToGet(itemstack));//init
		this.item = itemstack;
		this.need = needtype;
		if(statics.get(item.getItem()) == null)
			statics.put(item.getItem(), new ArrayList<AquireItemObjective>());
		
		statics.get(item.getItem()).add(this);
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
		if(item == null || item.getItem() == null || need == null)
			return null;
		return new AquireItemObjective(item, need);
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
				if (Item.getIdFromItem(item.getItem()) == Item.getIdFromItem(r.getRecipeOutput().getItem())) {
					possibilities.add(new CraftItemObjective(item, r));
					return possibilities;
				}
			}
		}
		System.out.println(Block.getIdFromBlock(Blocks.log) + " " + Item.getIdFromItem(item.getItem()) + " " + item + " " + item.getItem());
		if (Block.getIdFromBlock(Blocks.log) == Item.getIdFromItem(item.getItem())) {
			possibilities.add(new PunchTreeObjective());
		}
		System.out.println(item + " has options " + possibilities);
		return possibilities;
	}
	
	public double getCompletionPercentage() {
		return ((double) claim.getAmountCompleted()) / ((double) item.stackSize);
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
				if (Item.getIdFromItem(item.getItem()) == Item.getIdFromItem(a.getItem()) && item.stackSize <= a.stackSize) {
					System.out.println("Finished because already has " + item);
					return true;
				}
			}
		}
		return false;
	}
	
}
/**
 * This enum represents whether the required item for an <code>AquireItemObjective</code> will be used up (used in crafting), or can be used multiple times (like a crafting table)
 *
 */
enum Need {MULTI,SINGLE}