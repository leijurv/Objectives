//package net.winterflake.objectives;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.winterflake.event.EventListener;
//import net.winterflake.event.EventManager;
//import net.winterflake.event.PlayerItemPickupEvent;
//
///**
// * An AquireItemObjective can "claim" an item as soon as it comes into the
// * inventory. As well as claiming items, this also keeps track of completion.
// *
// * @author leijurv
// * @author howardstark
// * @author ave4224
// */
//public class Claim {
//	
//	final Need need;
//	private final ItemStack item;
//	final AquireItemObjective aio;
//	private volatile boolean completed;// Helper variable, not completely needed
//	private volatile int completion;
//	private volatile boolean stillNeeded;
//	
//	/**
//	 * This is a <code>HashMap</code> of every outstanding claim of items.
//	 */
//	private static HashMap<Item, ArrayList<Claim>> claimList = new HashMap<>();
//	
//	public Claim(AquireItemObjective objective) {
//		aio = objective;
//		item = aio.item;
//		need = aio.need;
//		completion = 0;
//		registerClaim(this);
//		completed = false;
//	}
//	
//	// @Override
//	// public int compareTo(Claim o) {
//	// System.out.println("Comparing " + this + " to " + o);
//	// if(this.need != o.need){
//	// if(this.need == Need.MULTI)
//	// return
//	// }
//	// return new Double(this.aio.getPriority()).compareTo(o.aio.getPriority());
//	// // Compare priorities not adjusted priorities, because adjusted
//	// // priorities are derived from completion percentage
//	// }
//	/**
//	 * How many items have been completed already
//	 * 
//	 * @return how many items have been completed already
//	 */
//	public int getAmountCompleted() {
//		return completion;
//	}
//	
//	/**
//	 * Get the item claimed
//	 * 
//	 * @return the item
//	 */
//	public Item getItem() {
//		return item.getItem();
//	}
//	
//	/**
//	 * Get the stack size of the item claimed
//	 * 
//	 * @return the stack size
//	 */
//	public int getStackSize() {
//		return item.stackSize;
//	}
//	
//	/**
//	 * Is this claim completely fufilled?
//	 * 
//	 * @return whether it's finished
//	 */
//	public boolean isFinished() {
//		if (!stillNeeded) {
//			return true;
//		}
//		if (completion == item.stackSize) {
//			completed = true;
//			return true;
//		}
//		completed = false;
//		return false;
//	}
//	
//	/**
//	 * Called when this claim is fully or partially filled by items entering the
//	 * inventory
//	 * 
//	 * @param amount
//	 *            The amount of items fufilled
//	 * @return The amount leftover after this claim takes what it can. A return
//	 *         of 0 means that this claim took all the fufilled items, a nonzero
//	 *         return means that some were leftover
//	 */
//	private int onFufill(int amount) {
//		if (!stillNeeded) {
//			return amount;
//		}
//		int remaining = item.stackSize - completion;
//		if (amount <= remaining) {
//			completion += amount;
//			return 0;
//		}
//		completion += remaining;
//		amount -= remaining;
//		completed = true;
//		return amount;
//	}
//	
//	/**
//	 * Register a claim into the queue for its itemID
//	 *
//	 * @param claim
//	 *            the claim to register
//	 */
//	private static void registerClaim(Claim claim) {
//		if (claimList.get(claim.item.getItem()) == null)
//			claimList.put(claim.item.getItem(), new ArrayList<Claim>());
//		claimList.get(claim.item.getItem()).add(claim);
//	}
//	
//	/**
//	 * Get the claim with the highest priority in the queue for the given
//	 * itemID. Multiple use claims will be returned first.
//	 *
//	 * @param itemID
//	 *            the itemID
//	 * @return the highest priority multi claim then the highest priority single
//	 *         claim.
//	 */
//	public static Claim getHighestPriorityClaim(Item item) {
//		ArrayList<Claim> possibilities = claimList.get(item);
//		if (possibilities == null || possibilities.isEmpty())
//			return null;
//		System.out.println(possibilities);
//		Claim max = possibilities.get(0);
//		for (int i = 1; i < possibilities.size(); i++) {
//			if ((max.need == Need.SINGLE || possibilities.get(i).need == Need.MULTI) && possibilities.get(i).aio.getPriority() > max.aio.getPriority() && possibilities.get(i).stillNeeded)
//				max = possibilities.get(i);
//		}
//		return max;
//	}
//	
//	/**
//	 * When a new item stack enters the inventory this is called to see if there
//	 * are any claims for it.
//	 *
//	 * @param itemID
//	 *            The itemID
//	 * @param amount
//	 *            The amount
//	 * @return How many items were claimed
//	 */
//	public static int onItemStack(ItemStack item) {
//		System.out.println("On pickup " + item);
//		Claim claim = getHighestPriorityClaim(item.getItem());
//		if (claim == null || item.stackSize == 0 || !claim.stillNeeded)
//			return 0;
//		
//		int remaining = claim.onFufill(item.stackSize);
//		int amountClaimed = item.stackSize - remaining;
//		if (remaining == 0) {// The claim was fufilled
//			System.out.println("Claim " + claim + " for " + item.getItem() + " was completely fulfilled");
//			claimList.get(item.getItem()).remove(claim);
//			return amountClaimed;
//		}
//		ItemStack stack = new ItemStack(item.getItem(), remaining, item.getMetadata());
//		return onItemStack(stack) + amountClaimed;
//	}
//	
//	public String toString() {
//		return item + "$" + aio.getAdjustedPriority();
//	}
//	
//	public void onUsedUp() {
//		stillNeeded = aio.stillNeeded();
//		if (!stillNeeded) {
//			ArrayList<Claim> m = claimList.get(item.getItem());
//			while (m.contains(this)) {
//				m.remove(this);
//			}
//		}
//	}
//}