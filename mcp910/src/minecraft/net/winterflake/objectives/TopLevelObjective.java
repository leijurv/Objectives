package net.winterflake.objectives;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;

/**
 *
 * @author leijurv
 */
public class TopLevelObjective implements Parent {
	private final MultiObjective child;
	private final double priority;

	public TopLevelObjective(ArrayList<Objective> toDo, double priority) {
		child = new SimpleEqualMultiObjective(toDo);
		child.registerParent(this);
		child.calculatePriority();
		this.priority = priority;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof TopLevelObjective) {
			return ((TopLevelObjective) o).child.equals(child);
		}
		return false;
	}

	@Override
	public double getPriority(Objective o) {
		if (o.equals(child)) {
			return priority;
		}
		return 0;
	}

	@Override
	public boolean hasChild(Objective child) {
		return child.equals(this.child);
	}

	public boolean onTick(Minecraft mc) {
		return child.onTick(mc);
	}
}
