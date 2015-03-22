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
public class TopLevelObjective extends Objective {
    private MultiObjective child;
    public TopLevelObjective(ArrayList<ChildObjective> toDo, double priority) {
        child = new SimpleEqualMultiObjective(toDo);
        this.priority = priority;
    }
    @Override
    public boolean equals(Object o) {
        if (o instanceof TopLevelObjective) {
            return ((TopLevelObjective) o).child.equals(child);
        }
        return false;
    }
}
