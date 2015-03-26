package net.winterflake.objectives;

import java.util.ArrayList;

/**
 *
 * @author leijurv
 */
public abstract class MultiAndObjective extends MultiObjective {
	public MultiAndObjective(ArrayList<Objective> childObjectives) {
		super(childObjectives);
	}

	/**
	 * If a set of objectives has to be completed, the difficulty is the sum of
	 * their difficulties
	 *
	 * @return
	 */
	@Override
	public double getDifficulty() {
		double sum = 0;
		for (Objective child : childObjectives) {
			sum += child.getDifficulty();
		}
		difficulty = sum;
		return difficulty;
	}
}
