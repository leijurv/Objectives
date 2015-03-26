package net.winterflake.objectives;

import java.util.HashMap;
import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

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

		return possibilities;
	}

	public double getCompletionPercentage() {
		return ((double) claim.getAmountCompleted())
				/ ((double) item.stackSize);
	}

}
