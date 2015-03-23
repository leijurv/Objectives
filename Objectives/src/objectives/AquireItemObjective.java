package objectives;
import java.util.HashMap;
/**
 *
 * @author leijurv
 */
public class AquireItemObjective extends ChildObjective {
    final int itemID;
    final int amount;
    final Claim claim;
    private AquireItemObjective(int itemID, int amount) {
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
    @Override
    public boolean equals(Object o) {
        if (o instanceof AquireItemObjective) {
            return ((AquireItemObjective) o).itemID == itemID && ((AquireItemObjective) o).amount == amount;
        }
        return false;
    }
    public double getDifficulty() {
        return 1 - getCompletionPercentage();
    }
    public double getCompletionPercentage() {
        return ((double) claim.getAmountCompleted()) / ((double) amount);
    }
}
