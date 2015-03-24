package net.winterflake.objectives;
import java.util.ArrayList;
/**
 *
 * @author leijurv
 */
public abstract class MultiObjective extends Objective implements Parent {
    protected final ArrayList<Objective> childObjectives;
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
        for (int i=0; i<childObjectives.size(); i++) {
        	final Objective child=childObjectives.get(i);
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
     * @param child The child
     * @return Do I have it
     */
    @Override
    public boolean hasChild(Objective child) {
        return childObjectives.contains(child);
    }
}
