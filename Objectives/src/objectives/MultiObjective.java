package objectives;
import java.util.ArrayList;
/**
 *
 * @author leijurv
 */
public abstract class MultiObjective extends ChildObjective implements Parent {
    protected final ArrayList<ChildObjective> childObjectives;
    public MultiObjective(ArrayList<ChildObjective> childObjectives) {
        this.childObjectives = childObjectives;
        for (ChildObjective child : childObjectives) {
            child.registerParent(this);
        }
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
    public boolean hasChild(ChildObjective child) {
        return childObjectives.contains(child);
    }
}
