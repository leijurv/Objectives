package net.winterflake.objectives;
/**
 *
 * @author leijurv
 */
public abstract class Objective implements Comparable {
    protected volatile double priority;
    protected volatile double difficulty;
    protected volatile boolean finished;
    public double getDifficulty() {
        return difficulty;
    }
    public double getPriority() {
        return priority;
    }
    public abstract double calculatePriority();
    public double getAdjustedPriority() {
        return getPriority() / getDifficulty();
    }
    public int compareTo(Objective o) {
        return new Double(getAdjustedPriority()).compareTo(o.getAdjustedPriority());
    }
    public boolean isFinished() {
        return finished;
    }
    @Override
    public int compareTo(Object o) {
        return compareTo((Objective) o);
    }
}
