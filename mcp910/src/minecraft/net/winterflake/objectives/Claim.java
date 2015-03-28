package net.winterflake.objectives;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * An AquireItemObjective can "claim" an item as soon as it comes into the
 * inventory. As well as claiming items, this also keeps track of completion.
 *
 * @author leijurv
 * @author howardstark
 * @author ave4224
 */
public class Claim{
	
	final Need need;
	private final ItemStack item;
	final AquireItemObjective aio;
	private volatile boolean completed;//Helper variable, not completely needed
	private volatile int completion;
	/**
	 * This is a <code>HashMap</code> of every outstanding claim of items.
	 */
	private static HashMap<Item, ArrayList<Claim>> claimList = new HashMap<>();
	
	public Claim(AquireItemObjective objective) {
		aio = objective;
		item = aio.item;
		need = aio.need;
		completion = 0;
		registerClaim(this);
		completed=false;
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
	/**
	 * How many items have been completed already
	 * @return how many items have been completed already
	 */
	public int getAmountCompleted() {
		return completion;
	}
	/**
	 * Get the item claimed
	 * @return the item
	 */
	public Item getItem(){
		return item.getItem();
	}
	/**
	 * Get the stack size of the item claimed
	 * @return the stack size
	 */
	public int getStackSize(){
		return item.stackSize;
	}
	/**
	 * Is this claim completely fufilled?
	 * 
	 * @return whether it's finished
	 */
	public boolean isFinished(){
		if(completion==item.stackSize){
			completed=true;
			return true;
		}
		completed=false;
		return false;
	}
	/**
	 * Called when this claim is fully or partially filled by items entering the inventory
	 * 
	 * @param amount The amount of items fufilled
	 * @return The amount leftover after this claim takes what it can. A return of 0 means that this claim took all the fufilled items, a nonzero return means that some were leftover
	 */
	private int onFufill(int amount) {
		int remaining = item.stackSize - completion;
		if(amount <= remaining){
			completion += amount;
			return 0;
		}
		completion += remaining;
		amount -= remaining;
		completed=true;
		// TODO remove self from list of outstanding claims, because I have been completely fufilled.
		return amount;
	}
	
	/**
	 * Register a claim into the queue for its itemID
	 *
	 * @param claim
	 *            the claim to register
	 */
	private static void registerClaim(Claim claim) {
		if (claimList.get(claim.item.getItem()) == null)
			claimList.put(claim.item.getItem(), new ArrayList<Claim>());
		claimList.get(claim.item.getItem()).add(claim);
	}
	
	/**
	 * Get the claim with the highest priority in the queue for the given itemID. Multiple use claims will be returned first.
	 *
	 * @param itemID the itemID
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
