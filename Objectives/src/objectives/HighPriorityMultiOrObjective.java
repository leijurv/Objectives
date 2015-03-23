package objectives;
import java.util.ArrayList;
/**
 *
 * @author leijurv
 */
public class HighPriorityMultiOrObjective extends MultiOrObjective {
    public HighPriorityMultiOrObjective(ArrayList<ChildObjective> childObjectives) {
        super(childObjectives);
    }
    @Override
    public double getPriority(ChildObjective o) {
        double oDiff = o.getDifficulty();
        for (ChildObjective c : childObjectives) {
            if (!c.equals(o)) {
                if (c.getDifficulty() < oDiff) {
                    return 0;
                }
            }
        }
        return getPriority();
    }
}
