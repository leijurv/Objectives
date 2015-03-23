package objectives;
import java.util.ArrayList;
/**
 *
 * @author leijurv
 */
public class Objectives {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        createParent(AquireItemObjective.getAquireItemObjective(1, 2, false), 1);
        createParent(AquireItemObjective.getAquireItemObjective(1, 3, true), 2);
        createParent(AquireItemObjective.getAquireItemObjective(1, 4, false), 1.5);
        System.out.println(Claim.getHighestPriorityClaim(1));
    }
    public static void createParent(ChildObjective o, double priority) {
        ArrayList<ChildObjective> toDo = new ArrayList<>();
        toDo.add(o);
        TopLevelObjective main = new TopLevelObjective(toDo, priority);
    }
}
