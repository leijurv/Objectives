package net.winterflake.objectives;
/**
 *
 * @author leijurv
 */
public class GetToCraftingTableObjective extends ChildObjective implements Parent {
    static boolean hasCraftingTable = false;
    private AquireItemObjective craftingtable;
    public GetToCraftingTableObjective() {
        if (!hasCraftingTable) {
            System.out.println("not has");
            craftingtable = AquireItemObjective.getAquireItemObjective(5, 1, false);
        }
    }
    @Override
    public boolean equals(Object o) {
        return o instanceof GetToCraftingTableObjective;
    }
    @Override
    public int hashCode() {
        return 0;
    }
    @Override
    public double getDifficulty() {
        if (hasCraftingTable) {
            return 1;
        }
        System.out.println("oaeuaoeuaeoauoe");
        return craftingtable.getDifficulty();
    }
    @Override
    public double getPriority(ChildObjective o) {
        if (hasChild(o)) {
            return getPriority();
        }
        return 0;
    }
    @Override
    public boolean hasChild(ChildObjective child) {
        return craftingtable.equals(child);
    }
}
