package net.winterflake.objectives;
import java.util.ArrayList;
/**
 *
 * @author leijurv
 */
public class SimpleEqualMultiObjective extends MultiAndObjective {
    public SimpleEqualMultiObjective(ArrayList<Objective> childObjectives) {
        super(childObjectives);
    }
    @Override
    public double getPriority(Objective o) {
        //System.out.println(this + "is calculating priority for child" + o);
        if (hasChild(o)) {
            return getPriority() * 1 / ((double) childObjectives.size());//Divvy up equally
        }
        System.out.println("not child");
        return 0;
    }
    @Override
    public boolean equals(Object o) {
        if (o instanceof SimpleEqualMultiObjective) {
            return ((SimpleEqualMultiObjective) o).childObjectives.equals(childObjectives);
        }
        return false;
    }
}
