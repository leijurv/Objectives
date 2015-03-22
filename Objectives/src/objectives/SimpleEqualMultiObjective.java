/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objectives;
import java.util.ArrayList;
/**
 *
 * @author leijurv
 */
public class SimpleEqualMultiObjective extends MultiObjective {
    public SimpleEqualMultiObjective(ArrayList<ChildObjective> childObjectives) {
        super(childObjectives);
    }
    @Override
    public double getPriority(ChildObjective o) {
        if (childObjectives.contains(o)) {
            return priority * 1 / ((double) childObjectives.size());//Divvy up equally
        }
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
