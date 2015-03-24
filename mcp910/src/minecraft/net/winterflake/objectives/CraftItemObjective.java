package net.winterflake.objectives;
import java.util.ArrayList;
/**
 *
 * @author leijurv
 */
public class CraftItemObjective extends MultiAndObjective {
    final int itemID;
    final int amount;
    public CraftItemObjective(int itemID, int amount, int[][] recipe) {
        super(getRequirements(recipe, amount, itemID));
        this.itemID = itemID;
        this.amount = amount;
    }
    public static ArrayList<ChildObjective> getRequirements(int[][] recipe, int outputAmount, int outputitemID) {
        ArrayList<ChildObjective> input = new ArrayList<>();
        if (requiresCraftingTable(recipe, outputitemID)) {
            System.out.println("Requires c");
            input.add(new GetToCraftingTableObjective());
        }
        for (int[] requirement : recipe) {
            int inputItemID = requirement[0];
            int imputAmount = requirement[1];
            imputAmount *= outputAmount;//multiple amount of input needed to make 1 output by the amount of output needed
            input.add(AquireItemObjective.getAquireItemObjective(inputItemID, imputAmount, true));
        }
        return input;
    }
    public static boolean requiresCraftingTable(int[][] recipe, int itemID) {
        int totAmt = 0;
        for (int[] x : recipe) {
            totAmt += x[1];
        }
        return totAmt > 4;
    }
    @Override
    public double getPriority(ChildObjective o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
