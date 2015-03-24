package net.winterflake.objectives;
import java.util.ArrayList;
/**
 *
 * @author leijurv
 */
public abstract class ChildObjective extends Objective {
    private final ArrayList<Parent> parentObjectives = new ArrayList<Parent>(); // all parent objectives must be multiobjectives
    /**
     * Recalculate my priority. My priority is the sum of all the priority
     * allocated to me by my parent objectives.
     *
     * @return the new priority
     */
    @Override
    public double calculatePriority() {
        double sum = 0;
        //System.out.println(this + " is calculating priority from parents " + parentObjectives);
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
     * @param parent the parent to register
     */
    public void registerParent(Parent parent) {
        if (!parent.hasChild(this)) {
            throw new IllegalStateException("YOU ARENT MY REAL DAD");
        }
        synchronized (parentObjectives) {
            parentObjectives.add(parent);
        }
    }
    @Override
    public abstract double getDifficulty();
}
