package objectives;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * An AquireItemObjective can "claim" an item as soon as it comes into the
 * inventory
 *
 * @author leijurv
 */
public class Claim implements Comparable {
    final int itemID;
    final int amount;
    final AquireItemObjective objective;
    private volatile int completion;
    static HashMap<Integer, ArrayList<Claim>> claimList = new HashMap<>();
    public Claim(AquireItemObjective obj) {
        objective = obj;
        itemID = obj.itemID;
        amount = obj.amount;
        registerClaim(this);
    }
    @Override
    public int compareTo(Object o) {
        System.out.println("Comparing " + this + " to " + o);
        return new Double(objective.getPriority()).compareTo(((Claim) o).objective.getPriority());//Compare priorities not adjusted priorities
    }
    public int getAmountCompleted() {
        return completion;
    }
    private void onFufill(int amountFufilled) {
        completion -= amountFufilled;
    }
    /**
     * Register a claim into the queue for its itemID
     *
     * @param claim the claim to register
     */
    public static void registerClaim(Claim claim) {
        if (claimList.get(claim.itemID) == null) {
            claimList.put(claim.itemID, new ArrayList<>());
        }
        claimList.get(claim.itemID).add(claim);
    }
    /**
     * Get the claim with the highest priority in the queue for the given itemID
     *
     * @param itemID the itemID
     * @return The claim with the highest priority
     */
    public static Claim getHighestPriorityClaim(int itemID) {
        ArrayList<Claim> possibilities = claimList.get(itemID);
        if (possibilities == null) {
            return null;
        }
        possibilities.sort(null);//sort(null) works because Claim implements Comparable
        return possibilities.get(possibilities.size() - 1);
    }
    /**
     * When a new item stack enters the inventory this is called to see if there
     * are any claims for it.
     *
     * @param itemID The itemID
     * @param amount The amount
     * @return Whether the item stack was claimed
     */
    public static boolean onItemStack(int itemID, int amount) {
        Claim claim = getHighestPriorityClaim(itemID);
        if (claim == null) {
            return false;
        }
        claim.onFufill(amount);
        return true;
    }
    public String toString() {
        return itemID + "-" + amount + "$" + objective.getAdjustedPriority();
    }
}
