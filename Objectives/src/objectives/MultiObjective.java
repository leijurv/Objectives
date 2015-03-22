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
    ArrayList<Objective> childObjectives;
    public abstract double getPriority(Objective o);//Get how much of my priority is allocated for o. If o is not in childObjectives, return 0
}
