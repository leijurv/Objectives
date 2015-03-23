package objectives;
/**
 *
 * @author leijurv
 */
public abstract class BaseObjective extends ChildObjective {
    protected long lastUpdate = -1;
    protected final long updatePeriod;
    protected volatile boolean currentlyCalculating;
    public BaseObjective(long updatePeriod) {
        this.updatePeriod = updatePeriod;
        currentlyCalculating = false;
    }
    /**
     * Calculate the difficulty for this objective. Is called in a separate
     * thread, so it can take as long as it needs
     *
     * @return the calculated difficulty
     */
    protected abstract double calculateDifficulty();
    @Override
    public double getDifficulty() {
        if (System.currentTimeMillis() > lastUpdate + updatePeriod && !currentlyCalculating) {
            recalculateDiff();
        }
        return difficulty;
    }
    private void recalculateDiff() {
        lastUpdate = System.currentTimeMillis();
        currentlyCalculating = true;
        new Thread() {
            public void run() {
                difficulty = calculateDifficulty();
                currentlyCalculating = false;
            }
        }.start();
    }
}
