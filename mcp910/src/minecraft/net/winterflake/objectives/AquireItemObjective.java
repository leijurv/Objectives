package net.winterflake.objectives;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.winterflake.event.EventListener;
import net.winterflake.event.PlayerItemPickupEvent;

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
	// final Claim claim;
	private volatile boolean completed;// Helper variable, not completely needed
	private volatile int completion;
	private volatile boolean stillNeeded = true;
	
	/**
	 * This is a <code>HashMap</code> of every outstanding instance of this
	 * class.
	 */
	private static HashMap<Item, ArrayList<AquireItemObjective>> claimList = new HashMap<>();
	
	private AquireItemObjective(ItemStack itemstack, Need needtype) {
		super(howToGet(itemstack));// init
		this.item = itemstack;
		this.need = needtype;
		registerClaim(this);
		// claim = new Claim(this);
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
		int available;
		/*
		 * if ((available = available(item.getItem())) >= item.stackSize) return
		 * possibilities;
		 * 
		 * item = new ItemStack(item.getItem(), item.stackSize - available);
		 */
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
		return ((double) completion) / ((double) item.stackSize);
	}
	
	public boolean onTick(Minecraft mc) {
		isFinished();
		return super.onTick(mc);
	}
	
	/**
	 * Is this claim completely fufilled?
	 * 
	 * @return whether it's finished
	 */
	// public boolean isFinished() {
	// if (!stillNeeded) {
	// return true;
	// }
	// if (completion == item.stackSize) {
	// completed = true;
	// return true;
	// }
	// completed = false;
	// return false;
	// }
	@Override
	public boolean isFinished() {
		System.out.println("Testing if aquireitemobjective for " + item + " is finished: sn:" + stillNeeded + " comp:" + completed + " comple:" + completion + " n:" + item.stackSize);
		
		return (finished = (!stillNeeded || completed || completion == item.stackSize));// (finished
		// =
		// claim.isFinished());
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
	
	public void onUsedUp() {
		System.out.println(item + " is used up.");
		stillNeeded = false;
		// claim.onUsedUp();
		finished = true;
		ArrayList<AquireItemObjective> m = claimList.get(item.getItem());
		while (m.contains(this)) {
			m.remove(this);
		}
	}
	
	public boolean stillNeeded() {
		return stillNeeded;
	}
	
	/**
	 * Called when this claim is fully or partially filled by items entering the
	 * inventory
	 * 
	 * @param amount
	 *            The amount of items fufilled
	 * @return The amount leftover after this claim takes what it can. A return
	 *         of 0 means that this claim took all the fufilled items, a nonzero
	 *         return means that some were leftover
	 */
	private int onFufill(int amount) {
		if (!stillNeeded) {
			return amount;
		}
		int remaining = item.stackSize - completion;
		if (amount <= remaining) {
			completion += amount;
			return 0;
		}
		completion += remaining;
		amount -= remaining;
		completed = true;
		return amount;
	}
	
	/**
	 * Register a claim into the queue for its itemID
	 *
	 * @param claim
	 *            the claim to register
	 */
	private static void registerClaim(AquireItemObjective claim) {
		if (claimList.get(claim.item.getItem()) == null)
			claimList.put(claim.item.getItem(), new ArrayList<AquireItemObjective>());
		claimList.get(claim.item.getItem()).add(claim);
	}
	
	/**
	 * Get the claim with the highest priority in the queue for the given
	 * itemID. Multiple use claims will be returned first.
	 *
	 * @param item
	 *            the item
	 * @return the highest priority multi claim then the highest priority single
	 *         claim.
	 */
	public static AquireItemObjective getHighestPriorityClaim(Item item) {
		ArrayList<AquireItemObjective> possibilities = claimList.get(item);
		if (possibilities == null || possibilities.isEmpty())
			return null;
		System.out.println(possibilities);
		
		AquireItemObjective max = possibilities.get(0);
		for (int i = 1; i < possibilities.size(); i++) {
			if (!possibilities.get(i).completed && possibilities.get(i).stillNeeded) {
				if (possibilities.get(i).getPriority() > max.getPriority()) {
					max = possibilities.get(i);
				}
			}
		}
		return max;
	}
	
	/**
	 * When a new item stack enters the inventory this is called to see if there
	 * are any claims for it.
	 *
	 * @param itemID
	 *            The itemID
	 * @param amount
	 *            The amount
	 * @return How many items were claimed
	 */
	public static int onItemStack(ItemStack item) {
		if (item == null)
			return 0;
		
		AquireItemObjective claim = getHighestPriorityClaim(item.getItem());
		System.out.print("On pickup " + item);
		System.out.println(" giving to " + claim);
		if (claim == null || item.stackSize == 0 || !claim.stillNeeded)
			return 0;
		
		int remaining = claim.onFufill(item.stackSize);
		int amountClaimed = item.stackSize - remaining;
		System.out.println("Claim " + claim + " for " + item.getItem() + " was claimed " + amountClaimed);
		if (remaining == 0) {// The claim was fufilled
			System.out.println("Claim " + claim + " for " + item.getItem() + " was completely fulfilled");
			// claimList.get(item.getItem()).remove(claim);
			return amountClaimed;
		}
		if (amountClaimed == 0) {
			throw new IllegalStateException("Nothing claimed!");
		}
		ItemStack stack = new ItemStack(item.getItem(), remaining, item.getMetadata());
		return onItemStack(stack) + amountClaimed;
	}
	
	public static void updateClaims() {
		if (Minecraft.getMinecraft() == null)
			throw new NullPointerException("Minecraft.getMinecraft() is null");
		Set<Item> items = claimList.keySet();
		System.out.println("REDISTRIBUTING");
		for (Item item : items) {
			ArrayList<AquireItemObjective> claims = claimList.get(item);
			for (int i = 0; i < claims.size(); i++) {
				if (!claims.get(i).stillNeeded) {
					claims.remove(i--);
				} else {
					System.out.println("Clearing " + claims.get(i));
					claims.get(i).completion = 0;
					claims.get(i).completed = false;
				}
			}
		}
		if (Minecraft.getMinecraft().thePlayer == null)
			throw new NullPointerException("thePlayer is null");
		ItemStack[] stacks = Minecraft.getMinecraft().thePlayer.inventory.mainInventory;
		for (ItemStack stack : stacks)
			onItemStack(stack);
	}
	
	public static int available(Item item) {
		if (item == null)
			return Integer.MAX_VALUE;
		updateClaims();
		int count = 0;
		ItemStack template = new ItemStack(item);
		for (ItemStack stack : Minecraft.getMinecraft().thePlayer.inventory.mainInventory)
			if (stack != null && stack.isItemEqual(template))
				count += stack.stackSize;
		if (claimList.get(item) == null)
			return count;
		for (AquireItemObjective a : claimList.get(item))
			if (a.stillNeeded)
				count -= a.item.stackSize;
		return count;
	}
	
	public String toString() {
		return "AquireITem " + item + " " + completion;
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

class ClaimListener extends EventListener {
	
	@Override
	public void onEvent() {
		AquireItemObjective.onItemStack(((PlayerItemPickupEvent) event).getItem().getEntityItem());
	}
	
	@Override
	public Class getEventType() {
		return PlayerItemPickupEvent.class;
	}
}