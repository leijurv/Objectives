/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objectives;
/**
 *
 * @author leijurv
 */
public abstract class Objective implements Comparable {
    protected volatile double priority;
    protected volatile double difficulty;
    public double getDifficulty() {
        return difficulty;
    }
    public double getPriority() {
        return priority;
    }
    public double getAdjustedPriority() {
        return priority / difficulty;
    }
    public int compareTo(Objective o) {
        return new Double(getAdjustedPriority()).compareTo(o.getAdjustedPriority());
    }
    @Override
    public int compareTo(Object o) {
        return compareTo((Objective) o);
    }
    public abstract boolean equals(Object o);//This is needed for stuff like ArrayLists and sorting and crap
}
