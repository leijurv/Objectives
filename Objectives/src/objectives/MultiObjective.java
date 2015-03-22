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
public abstract class MultiObjective extends Objective {
    protected final ArrayList<ChildObjective> childObjectives;
    public MultiObjective(ArrayList<ChildObjective> childObjectives) {
        this.childObjectives = childObjectives;
    }
    public abstract double getPriority(ChildObjective o);//Get how much of my priority is allocated for o. If o is not in childObjectives, return 0
    @Override
    public double getDifficulty() {
        double sum = 0;
        for (Objective child : childObjectives) {
            sum += child.getDifficulty();
        }
        difficulty = sum;
        return super.getDifficulty();
    }
}
