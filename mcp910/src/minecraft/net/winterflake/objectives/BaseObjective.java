package net.winterflake.objectives;

/**
 *
 * @author leijurv
 */
public abstract class BaseObjective extends Objective {
	
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
		if (System.currentTimeMillis() > lastUpdate + updatePeriod) {
			recalculateDiff();
		}
		return difficulty;
	}
	
	/**
	 * Reclaculate the difficulty for this baseobjective. Multithreaded because
	 * this might take a long time if it's something like pathfinding
	 */
	public void recalculateDiff() {
		if (!currentlyCalculating) {
			lastUpdate = System.currentTimeMillis();
			currentlyCalculating = true;
			final String abcd = toString();
			new Thread() {
				
				public void run() {
					difficulty = calculateDifficulty();
					System.out.println("Recalculated diff for" + abcd + " as " + difficulty);
					currentlyCalculating = false;
				}
			}.start();
		}
	}
}
