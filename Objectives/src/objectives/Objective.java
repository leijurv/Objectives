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
        return getPriority() / getDifficulty();
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
