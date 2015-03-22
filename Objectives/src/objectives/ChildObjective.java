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
public abstract class ChildObjective extends Objective {
    protected ArrayList<MultiObjective> parentObjectives; // all parent objectives must be multiobjectives
    @Override
    public double getPriority() {
        double sum = 0;
        for (MultiObjective mo : parentObjectives) {
            sum += mo.getPriority(this);
        }
        priority = sum;
        return super.getPriority();
    }
}
