package net.winterflake.objectives;
import java.util.ArrayList;
/**
 *
 * @author leijurv
 */
public abstract class MultiOrObjective extends MultiObjective {
    public MultiOrObjective(ArrayList<Objective> childObjectives) {
        super(childObjectives);
    }
    /**
     * The difficulty of an objective where any of the options can be used is
     * the difficulty of the easiest option
     *
     * @return
     */
    @Override
    public double getDifficulty() {
        double min = -1;
        for (Objective child : childObjectives) {
            double d = child.getDifficulty();
            if (min == -1 || d < min) {
                min = d;
            }
        }
        return min;
    }
}
