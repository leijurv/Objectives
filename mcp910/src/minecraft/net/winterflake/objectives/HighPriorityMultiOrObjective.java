package net.winterflake.objectives;

import java.util.ArrayList;

/**
 *
 * @author leijurv
 */
public class HighPriorityMultiOrObjective extends MultiOrObjective {
	public HighPriorityMultiOrObjective(ArrayList<Objective> childObjectives) {
		super(childObjectives);
	}

	@Override
	public double getPriority(Objective o) {
		double oDiff = o.getDifficulty();
		for (Objective c : childObjectives) {
			if (!c.equals(o)) {
				if (c.getDifficulty() < oDiff) {
					return 0;
				}
			}
		}
		return getPriority();
	}
}
