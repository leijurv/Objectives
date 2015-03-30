package net.winterflake.objectives;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;

/**
 *
 * @author leijurv
 */
public abstract class MultiObjective extends Objective implements Parent,
		UsedUp {
	
	protected final ArrayList<Objective> childObjectives;
	protected int position = 0;
	
	public MultiObjective(ArrayList<Objective> childObjectives) {
		this.childObjectives = childObjectives;
		for (Objective child : childObjectives) {
			child.registerParent(this);
		}
	}
	
	/**
	 * We need to notify all children to recalculate priority when our priority
	 * changes
	 *
	 *
	 * @return the new priority
	 */
	@Override
	public double calculatePriority() {
		super.calculatePriority();
		for (int i = 0; i < childObjectives.size(); i++) {
			final Objective child = childObjectives.get(i);
			new Thread() {
				
				@Override
				public void run() {
					child.calculatePriority();
				}
			}.start();
		}
		return priority;
	}
	
	/**
	 * Get the difficulty of the objectives
	 *
	 * @return the difficulty
	 */
	@Override
	public abstract double getDifficulty();
	
	/**
	 * Do I have this child
	 *
	 * @param child
	 *            The child
	 * @return Do I have it
	 */
	@Override
	public boolean hasChild(Objective child) {
		return childObjectives.contains(child);
	}
	
	/**
	 * Go through each child objective sequentially
	 */
	public void doTick(Minecraft mc) {
		if (childObjectives.isEmpty()) {
			// System.out.println(this + " has no child objectives.");
			return;
		}
		if (position >= childObjectives.size()) {
			System.out.println("Finished totally" + childObjectives + " " + position + " " + finished + " " + this);
			finished = true;
			return;
		}
		System.out.println(this + " is on position " + position + " which is " + childObjectives.get(position));
		if (!childObjectives.get(position).onTick(mc)) {
			position++;
			System.out.println("Moved onto position " + position + ", which is " + (position >= childObjectives.size() ? "the end" : childObjectives.get(position).toString()));
		}
	}
	
	@Override
	public void onUsedUp() {
		for (Objective c : childObjectives) {
			if (c instanceof UsedUp) {
				((UsedUp) c).onUsedUp();
			}
		}
	}
}
