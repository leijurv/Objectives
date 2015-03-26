package net.winterflake.objectives;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * An AquireItemObjective can "claim" an item as soon as it comes into the
 * inventory
 *
 * @author leijurv
 */
public class Claim implements Comparable {
	final ItemStack item;
	final AquireItemObjective objective;
	private volatile int completion;
	static HashMap<Item, ArrayList<Claim>> claimList = new HashMap<>();

	public Claim(AquireItemObjective obj) {
		objective = obj;
		item = obj.item;
		registerClaim(this);
	}

	@Override
	public int compareTo(Object o) {
		System.out.println("Comparing " + this + " to " + o);
		return new Double(objective.getPriority())
				.compareTo(((Claim) o).objective.getPriority());
		// Compare priorities not adjusted priorities, because adjusted
		// priorities are derived from completion percentage
	}

	public int getAmountCompleted() {
		return completion;
	}

	private void onFufill(int amountFufilled) {
		completion -= amountFufilled;
	}

	/**
	 * Register a claim into the queue for its itemID
	 *
	 * @param claim
	 *            the claim to register
	 */
	public static void registerClaim(Claim claim) {
		if (claimList.get(claim.item.getItem()) == null) {
			claimList.put(claim.item.getItem(), new ArrayList<Claim>());
		}
		claimList.get(claim.item.getItem()).add(claim);
	}

	/**
	 * Get the claim with the highest priority in the queue for the given itemID
	 *
	 * @param itemID
	 *            the itemID
	 * @return The claim with the highest priority
	 */
	public static Claim getHighestPriorityClaim(int itemID) {
		ArrayList<Claim> possibilities = claimList.get(itemID);
		if (possibilities == null) {
			return null;
		}
		System.out.println(possibilities);
		possibilities.sort(null);// sort(null) works because Claim implements
									// Comparable
		return possibilities.get(possibilities.size() - 1);
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
	public static boolean onItemStack(int itemID, int amount) {
		Claim claim = getHighestPriorityClaim(itemID);
		if (claim == null) {
			return false;
		}
		claim.onFufill(amount);
		return true;
	}

	public String toString() {
		return item + "$" + objective.getAdjustedPriority();
	}
}
