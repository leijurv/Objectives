package net.winterflake.objectives;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

/**
 *
 * @author leijurv
 */
public class GetToCraftingTableObjective extends Objective implements Parent {
    static boolean hasCraftingTable = false;
    private AquireItemObjective craftingtable;
    public GetToCraftingTableObjective() {
        if (!hasCraftingTable) {
            craftingtable = AquireItemObjective.getAquireItemObjective(new ItemStack(Blocks.crafting_table,1), false);
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
    public double getPriority(Objective o) {
        if (hasChild(o)) {
            return getPriority();
        }
        return 0;
    }
    @Override
    public boolean hasChild(Objective child) {
        return craftingtable.equals(child);
    }
}
