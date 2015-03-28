package net.winterflake.objectives;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * An AquireItemObjective can "claim" an item as soon as it comes into the
 * inventory. As well as claiming items, this also keeps track of completion.
 *
 * @author Leif Jurvetson
 * @author Mickey Daras
 * @author Avery Cowan
 */
public class Claim{
	
	final Need need;
	final ItemStack item;
	final AquireItemObjective aio;
	private volatile int completion;
	/**
	 * This is a <code>HashMap</code> of every outstanding claim of items.
	 */
	static HashMap<Item, ArrayList<Claim>> claimList = new HashMap<>();
	
	public Claim(AquireItemObjective objective) {
		aio = objective;
		item = aio.item;
		need = aio.need;
		completion = 0;
		registerClaim(this);
	}
	
//	@Override
//	public int compareTo(Claim o) {
//		System.out.println("Comparing " + this + " to " + o);
//		if(this.need != o.need){
//			if(this.need == Need.MULTI)
//				return 
//		}
//		return new Double(this.aio.getPriority()).compareTo(o.aio.getPriority());
//		// Compare priorities not adjusted priorities, because adjusted
//		// priorities are derived from completion percentage
//	}
	
	public int getAmountCompleted() {
		return completion;
	}
	
	private int onFufill(int amount) {
		int remaining = item.stackSize - completion;
		if(amount <= remaining){
			completion += amount;
			return 0;
		}
		completion += remaining;
		amount -= remaining;
		return amount;
	}
	
	/**
	 * Register a claim into the queue for its itemID
	 *
	 * @param claim
	 *            the claim to register
	 */
	public static void registerClaim(Claim claim) {
		if (claimList.get(claim.item.getItem()) == null)
			claimList.put(claim.item.getItem(), new ArrayList<Claim>());
		claimList.get(claim.item.getItem()).add(claim);
	}
	
	/**
	 * Get the claim with the highest priority in the queue for the given itemID. Multiple use claims will be returned first.
	 *
	 * @param itemID
	 *            the itemID
	 * @return the highest priority multi claim then the highest priority single claim.
	 */
	public static Claim getHighestPriorityClaim(Item item) {
		ArrayList<Claim> possibilities = claimList.get(item);
		if (possibilities == null || possibilities.isEmpty())
			return null;
		System.out.println(possibilities);
		Claim max = possibilities.get(0);
		for(int i = 1; i < possibilities.size(); i++){
			if((max.need == Need.SINGLE || possibilities.get(i).need == Need.MULTI) && possibilities.get(i).aio.getPriority() > max.aio.getPriority())
				max = possibilities.get(i);
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
	 * @return Whether the item stack was claimed
	 */
	public static boolean onItemStack(ItemStack item) {
		Claim claim = getHighestPriorityClaim(item.getItem());
		if (claim == null || item.stackSize == 0)
			return false;
		
		int remaining = claim.onFufill(item.stackSize);
		if(remaining == 0)
			return true;
		
		ItemStack stack = new ItemStack(item.getItem(), remaining, item.getMetadata());
		onItemStack(stack);
		return true;
	}
	
	public String toString() {
		return item + "$" + aio.getAdjustedPriority();
	}
}
