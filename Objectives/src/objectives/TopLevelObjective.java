package objectives;
import java.util.ArrayList;
/**
 *
 * @author leijurv
 */
public class TopLevelObjective extends Objective implements Parent {
    private MultiObjective child;
    public TopLevelObjective(ArrayList<ChildObjective> toDo, double priority) {
        child = new SimpleEqualMultiObjective(toDo);
        child.registerParent(this);
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
    public double getPriority(ChildObjective o) {
        if (o.equals(child)) {
            return priority;
        }
        return 0;
    }
    @Override
    public boolean hasChild(ChildObjective child) {
        return child.equals(this.child);
    }
}
