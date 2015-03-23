package objectives;
import java.util.ArrayList;
/**
 *
 * @author leijurv
 */
public abstract class ChildObjective extends Objective {
    private final ArrayList<Parent> parentObjectives = new ArrayList<>(); // all parent objectives must be multiobjectives
    /**
     * Sum up the priority allocated to me by all my parents
     *
     * @return the summed priority
     */
    @Override
    public double getPriority() {
        double sum = 0;
        //System.out.println(this + " is calculating priority from parents " + parentObjectives);
        synchronized (parentObjectives) {
            for (Parent mo : parentObjectives) {
                System.out.println(mo.getPriority(this));
                sum += mo.getPriority(this);
            }
        }
        priority = sum;
        return super.getPriority();
    }
    /**
     * register a parent objective. Called in the MultiObjective constructor for
     * all its children
     *
     * @param parent the parent to register
     */
    public void registerParent(Parent parent) {
        if (!parent.hasChild(this)) {
            throw new IllegalStateException("YOU ARENT MY REAL DAD");
        }
        synchronized (parentObjectives) {
            parentObjectives.add(parent);
        }
    }
}
