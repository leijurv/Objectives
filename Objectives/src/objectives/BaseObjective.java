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
public abstract class BaseObjective extends Objective {
    protected ArrayList<MultiObjective> parentObjectives; // all parent objectives must be multiobjectives
    protected long lastUpdate;
    protected long updatePeriod;
    protected boolean currentlyCalculating;
    protected abstract double calculateDifficulty(); // is abstract. Function can be called by anyone, but it might take a while
    @Override
    public double getDifficulty() {
        if (System.currentTimeMillis() > lastUpdate + updatePeriod && !currentlyCalculating) {
            recalculateDiff();
        }
        return difficulty;
    }
    @Override
    public double getPriority() {
        double sum = 0;
        for (MultiObjective mo : parentObjectives) {
            sum += mo.getPriority(this);
        }
        priority = sum;
        return super.getPriority();
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
