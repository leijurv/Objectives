package net.winterflake.objectives;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;

/**
 *
 * @author leijurv
 */
public abstract class Objective implements Comparable {
	protected volatile double priority;
	protected volatile double difficulty;
	protected volatile boolean finished = false;

	public double getDifficulty() {
		return difficulty;
	}

	public double getPriority() {
		return priority;
	}

	public double getAdjustedPriority() {
		return getPriority() / getDifficulty();
	}

	public int compareTo(Objective o) {
		return new Double(getAdjustedPriority()).compareTo(o
				.getAdjustedPriority());
	}

	public boolean isFinished() {
		return finished;
	}

	@Override
	public int compareTo(Object o) {
		return compareTo((Objective) o);
	}

	private final ArrayList<Parent> parentObjectives = new ArrayList<Parent>(); // all
																				// parent
																				// objectives
																				// must
																				// be
																				// multiobjectives

	/**
	 * Recalculate my priority. My priority is the sum of all the priority
	 * allocated to me by my parent objectives.
	 *
	 * @return the new priority
	 */
	public double calculatePriority() {
		double sum = 0;
		// System.out.println(this + " is calculating priority from parents " +
		// parentObjectives);
		synchronized (parentObjectives) {
			for (Parent parent : parentObjectives) {
				sum += parent.getPriority(this);
			}
		}
		priority = sum;
		return priority;
	}

	/**
	 * register a parent objective. Called in the MultiObjective constructor for
	 * all its children
	 *
	 * @param parent
	 *            the parent to register
	 */
	public void registerParent(Parent parent) {
		if (!parent.hasChild(this)) {
			throw new IllegalStateException("YOU ARENT MY REAL DAD");
		}
		synchronized (parentObjectives) {
			parentObjectives.add(parent);
		}
	}

	public boolean onTick(Minecraft mc) {
		if (!isFinished()) {
			doTick(mc);
		}
		return !isFinished();
	}

	protected abstract void doTick(Minecraft mc);
}
