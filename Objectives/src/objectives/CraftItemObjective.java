package objectives;
import java.util.ArrayList;
/**
 *
 * @author leijurv
 */
public class CraftItemObjective extends MultiAndObjective {
    final int itemID;
    final int amount;
    public CraftItemObjective(ArrayList<ChildObjective> childObjectives, int itemID, int amount, int[][] recipe) {
        super(childObjectives);
        this.itemID = itemID;
        this.amount = amount;
    }
    public static ArrayList<AquireItemObjective> getRequirements(int[][] recipe, int outputAmount) {
        ArrayList<AquireItemObjective> input = new ArrayList<>();
        for (int[] requirement : recipe) {
            int inputItemID = requirement[0];
            int imputAmount = requirement[1];
            imputAmount *= outputAmount;//multiple amount of input needed to make 1 output by the amount of output needed
            input.add(AquireItemObjective.getAquireItemObjective(inputItemID, imputAmount, true));
        }
        return input;
    }
    @Override
    public boolean equals(Object o) {
        if (o instanceof CraftItemObjective) {
            return ((CraftItemObjective) o).itemID == itemID && ((CraftItemObjective) o).amount == amount;
        }
        return false;
    }
    @Override
    public double getPriority(ChildObjective o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
