package objectives;
import java.util.HashMap;
import java.util.ArrayList;
/**
 *
 * @author leijurv
 */
public class AquireItemObjective extends HighPriorityMultiOrObjective {
    final int itemID;
    final int amount;
    final Claim claim;
    private AquireItemObjective(int itemID, int amount) {
        super(howToGet(itemID, amount));
        this.itemID = itemID;
        this.amount = amount;
        claim = new Claim(this);
    }
    private static final HashMap<Integer, AquireItemObjective> statics = new HashMap<>();
    /**
     *
     * @param itemID the item id
     * @param amount the amount
     * @param need whether the item will be used up (used in crafting), or can
     * be used multiple times (like a crafting table)
     * @return
     */
    public static AquireItemObjective getAquireItemObjective(int itemID, int amount, boolean need) {
        if (need) {
            return new AquireItemObjective(itemID, amount);
        }
        if (statics.get(itemID) != null) {
            return statics.get(itemID);
        }
        AquireItemObjective o = new AquireItemObjective(itemID, amount);
        statics.put(itemID, o);
        return o;
    }
    public double getDifficulty() {
        System.out.println("Aquiring");
        return (1 - getCompletionPercentage()) * super.getDifficulty();
    }
    public static ArrayList<ChildObjective> howToGet(int itemID, int amount) {
        ArrayList<ChildObjective> possibilities = new ArrayList<>();
        switch (itemID) {
            case 1:
                possibilities.add(new CraftItemObjective(itemID, amount, new int[][] {{3, 1}}));
                break;
            case 3:
                break;
            case 5:
                System.out.println("crafting table");
                possibilities.add(new CraftItemObjective(itemID, amount, new int[][] {{1, 4}}));
                break;
            case 7:
                System.out.println("catss");
                possibilities.add(new CraftItemObjective(itemID, amount, new int[][] {{1, 4}, {3, 1}, {5, 1}}));
                break;
        }
        return possibilities;
    }
    public double getCompletionPercentage() {
        return ((double) claim.getAmountCompleted()) / ((double) amount);
    }
}
