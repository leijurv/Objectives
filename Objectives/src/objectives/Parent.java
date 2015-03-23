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
public interface Parent {
    /**
     * Get how much of my priority is allocated for o. If o is not in
     * childObjectives, return 0. Must be fast.
     *
     * @param o The child in question
     * @return How much priority is allocated for that child
     */
    public abstract double getPriority(ChildObjective o);
    /**
     * Do I have this child
     *
     * @param child The child
     * @return Do I have it
     */
    public boolean hasChild(ChildObjective child);
}